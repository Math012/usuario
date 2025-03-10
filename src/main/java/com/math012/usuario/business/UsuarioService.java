package com.math012.usuario.business;

import com.math012.usuario.business.converter.UsuarioConverter;
import com.math012.usuario.business.dto.EnderecoDTO;
import com.math012.usuario.business.dto.TelefoneDTO;
import com.math012.usuario.business.dto.UsuarioDTO;
import com.math012.usuario.infrastructure.entity.Endereco;
import com.math012.usuario.infrastructure.entity.Telefone;
import com.math012.usuario.infrastructure.entity.Usuario;
import com.math012.usuario.infrastructure.exception.ConflictException;
import com.math012.usuario.infrastructure.exception.ResourceNotFoundException;
import com.math012.usuario.infrastructure.repository.EnderecoRepository;
import com.math012.usuario.infrastructure.repository.TelefoneRepository;
import com.math012.usuario.infrastructure.repository.UsuarioRepository;
import com.math012.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    public UsuarioDTO salvarUsuario(UsuarioDTO usuarioDTO){
        emailExists(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.usuarioDTOParaUsuario(usuarioDTO);
        return usuarioConverter.usuarioParaUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExists(String email){
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe){
                throw new ConflictException("Email já cadastrado");
            }
        }catch (ConflictException e){
            throw new ConflictException("Email já cadastrado: " + e.getCause());
        }
    }

    private boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email){
        try {
            return usuarioConverter.usuarioParaUsuarioDTO(
                    usuarioRepository.findByEmail(email)
                            .orElseThrow(
                    ()-> new ResourceNotFoundException("Email não encontrado: " + email)
            ));
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Email não encontrado: " + email);
        }
    }

    public void deletarUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosDeUsuario(UsuarioDTO dto, String token){
        Usuario usuarioEntity = usuarioRepository.findByEmail(jwtUtil.extractUsername(token.substring(7))).orElseThrow(()->
                new ResourceNotFoundException("Email não localizado")
                );

        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);

        Usuario usuario = usuarioConverter.updateUsuario(dto,usuarioEntity);

        return usuarioConverter.usuarioParaUsuarioDTO(usuarioRepository.save(usuario));

    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO){
        Endereco enderecoEntity = enderecoRepository.findById(idEndereco).orElseThrow(() ->
                new ResourceNotFoundException("Endereço não encontrado!"));
        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, enderecoEntity);
        return usuarioConverter.enderecoParaEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO telefoneDTO){
        Telefone telefoneEntity = telefoneRepository.findById(idTelefone).orElseThrow(() ->
                new ResourceNotFoundException("Telefone não encontrado!"));

        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO,telefoneEntity);
        return usuarioConverter.telefoneParaTelefoneDTO(telefoneRepository.save(telefone));
    }

    public EnderecoDTO cadastrarEndereco(String token, EnderecoDTO enderecoDTO){

        String email = jwtUtil.extractUsername(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email não encontrado!"));

        Endereco endereco = usuarioConverter.paraEnderecoEntity(enderecoDTO, usuario.getId());
        return usuarioConverter.enderecoParaEnderecoDTO(enderecoRepository.save(endereco));

    }

    public TelefoneDTO cadastrarTelefone(String token, TelefoneDTO telefoneDTO){
        String email = jwtUtil.extractUsername(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException("Email não encontrado!"));

        Telefone telefone = usuarioConverter.paraTelefoneEntity(telefoneDTO, usuario.getId());
        return usuarioConverter.telefoneParaTelefoneDTO(telefoneRepository.save(telefone));


    }


}
