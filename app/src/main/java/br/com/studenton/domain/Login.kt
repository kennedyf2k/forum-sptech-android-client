package br.com.studenton.domain

data class Login(

    var idUsuario: Int,
    var ra: String,
    var nome: String,
    var email: String,
    var curso: String,
    var semestre: Int,
    var fotoPerfil: String,
    var fkAcesso: Int,
    var checkEmail: Boolean,
    var autenticado: Boolean


)
