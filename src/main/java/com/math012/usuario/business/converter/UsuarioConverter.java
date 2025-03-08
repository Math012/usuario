package com.math012.usuario.business.converter;

import com.math012.usuario.business.dto.EnderecoDTO;
import com.math012.usuario.business.dto.TelefoneDTO;
import com.math012.usuario.business.dto.UsuarioDTO;
import com.math012.usuario.infrastructure.entity.Endereco;
import com.math012.usuario.infrastructure.entity.Telefone;
import com.math012.usuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioConverter {

    // Convertendo DTO para entidade

    public Usuario usuarioDTOParaUsuario(UsuarioDTO usuarioDTO){
        return Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(ListaEnderecoDTOParaListaEndereco(usuarioDTO.getEnderecos()))
                .telefones(ListaTelefoneDTOParaListaTelefone(usuarioDTO.getTelefones()))
                .build();
    }

    public List<Endereco> ListaEnderecoDTOParaListaEndereco(List<EnderecoDTO> enderecoDTO){
        return enderecoDTO.stream().map(this::enderecoDTOParaEndereco).toList();
    }

    public Endereco enderecoDTOParaEndereco(EnderecoDTO enderecoDTO){
        return Endereco.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .complemento(enderecoDTO.getComplemento())
                .cidade(enderecoDTO.getCidade())
                .estado(enderecoDTO.getEstado())
                .cep(enderecoDTO.getCep())
                .build();
    }

    public List<Telefone> ListaTelefoneDTOParaListaTelefone(List<TelefoneDTO> telefoneDTO){
        return telefoneDTO.stream().map(this::telefoneDTOParaTelefone).toList();
    }

    public Telefone telefoneDTOParaTelefone(TelefoneDTO telefoneDTO){
        return Telefone.builder()
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .build();
    }

    // Convertendo Entidade para DTO

    public UsuarioDTO usuarioParaUsuarioDTO(Usuario usuario){
        return UsuarioDTO.builder()
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .enderecos(ListaEnderecoParaListaEnderecoDTO(usuario.getEnderecos()))
                .telefones(listaTelefoneParaTelefoneDTO(usuario.getTelefones()))
                .build();
    }

    public List<EnderecoDTO> ListaEnderecoParaListaEnderecoDTO(List<Endereco> enderecos){
        return enderecos.stream().map(this::enderecoParaEnderecoDTO).toList();
    }

    public EnderecoDTO enderecoParaEnderecoDTO(Endereco endereco){
        return EnderecoDTO.builder()
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .complemento(endereco.getComplemento())
                .cidade(endereco.getCidade())
                .estado(endereco.getEstado())
                .cep(endereco.getCep())
                .build();
    }

    public List<TelefoneDTO> listaTelefoneParaTelefoneDTO(List<Telefone> telefones){
        return telefones.stream().map(this::telefoneParaTelefoneDTO).toList();
    }

    public TelefoneDTO telefoneParaTelefoneDTO(Telefone telefone){
        return TelefoneDTO.builder()
                .numero(telefone.getNumero())
                .ddd(telefone.getDdd())
                .build();
    }

    public Usuario updateUsuario(UsuarioDTO usuarioDTO, Usuario usuario){
        return Usuario.builder()
                .id(usuario.getId())
                .nome(usuarioDTO.getNome() != null ? usuarioDTO.getNome() : usuario.getNome())
                .email(usuarioDTO.getEmail() != null ? usuarioDTO.getEmail() : usuario.getEmail())
                .senha(usuarioDTO.getSenha() != null ? usuarioDTO.getSenha() : usuario.getSenha())
                .enderecos(usuario.getEnderecos())
                .telefones(usuario.getTelefones())
                .build();
    }
}
