package net.lwbuchanan.lox;

enum TokenType {
    // 1 character tokens
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    // 1-2 character tokens
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    // Literals
    IDENTIFIER, STRING, NUMBER,

    // Keywords
    CLASS, THIS, SUPER,
    FUN, RETURN, VAR, PRINT,
    TRUE, FALSE, NIL, AND, OR,
    IF, ELSE, FOR, WHILE,

    EOF
    
}
