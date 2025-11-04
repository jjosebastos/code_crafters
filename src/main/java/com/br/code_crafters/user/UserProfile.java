package com.br.code_crafters.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "t_mtu_user_profiles")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfile {

    @Id
    @Column(name = "user_id")
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "sobrenome")
    private String sobrenome;
    @Column(name = "telefone")
    private String telefone;
    @Column(name = "bio")
    private String bio;
    @Column(name = "foto_url")
    private String fotoUrl;
    @Column(name = "idioma")
    private String idioma;
    @Column(name = "tema")
    private String tema;
    @Column(name = "genero")
    private String genero;
    @Column(name = "notificacoes_ativas")
    private boolean notificacoesAtivas;



}