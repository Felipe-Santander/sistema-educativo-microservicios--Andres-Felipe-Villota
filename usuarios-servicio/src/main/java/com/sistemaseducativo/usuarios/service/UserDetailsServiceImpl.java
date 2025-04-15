package com.sistemaseducativo.usuarios.service;

import com.sistemaseducativo.usuarios.entity.Usuario;
import com.sistemaseducativo.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User; // Importante: usar el de Spring Security
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Buena práctica para servicios que acceden a BD

import java.util.HashSet;
import java.util.Set;

@Service // Marca esta clase como un servicio de Spring
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true) // Indica que este método es transaccional y de solo lectura
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Buscar el usuario en la base de datos por su email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                    new UsernameNotFoundException("Usuario no encontrado con el email: " + email)
                );

        // 2. Crear una colección de GrantedAuthority (roles/permisos)
        //    Por ahora, usaremos el campo 'tipoUsuario' para definir una autoridad simple.
        //    Ejemplo: Si tipoUsuario es "DOCENTE", el rol será "ROLE_DOCENTE".
        //    ¡IMPORTANTE! Spring Security espera que los roles empiecen con "ROLE_" por convención.
        Set<GrantedAuthority> authorities = new HashSet<>();
        String role = "ROLE_" + usuario.getTipoUsuario().toUpperCase(); // Convierte a mayúsculas y añade prefijo
        authorities.add(new SimpleGrantedAuthority(role));

        // Podríamos añadir más authorities/roles si los tuviéramos en la entidad Usuario

        // 3. Devolver un objeto UserDetails (usando la implementación User de Spring Security)
        //    Este objeto contiene la información que Spring Security necesita.
        return new User(
                usuario.getEmail(),         // Username (nuestro email)
                usuario.getPassword(),      // Contraseña CODIFICADA de la BD
                authorities                 // Colección de roles/permisos
                // Podemos añadir flags para cuenta expirada, bloqueada, etc., si fueran necesarios
                // true, true, true, true // enabled, accountNonExpired, credentialsNonExpired, accountNonLocked
        );
    }
}