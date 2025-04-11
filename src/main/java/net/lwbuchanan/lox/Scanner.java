package net.lwbuchanan.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.lwbuchanan.lox.TokenType.*;

class Scanner {
  private final String source;
  private final List<Token> tokens = new ArrayList<>();
  private int start = 0;
  private int current = 0;
  private int line = 1;

  private static final Map<String, TokenType> keywords;
  static {
    keywords = new HashMap<>();
    keywords.put("and", AND);
    keywords.put("class", CLASS);
    keywords.put("else", ELSE);
    keywords.put("false", FALSE);
    keywords.put("for", FOR);
    keywords.put("fun", FUN);
    keywords.put("if", IF);
    keywords.put("nil", NIL);
    keywords.put("or", OR);
    keywords.put("print", PRINT);
    keywords.put("return", RETURN);
    keywords.put("super", SUPER);
    keywords.put("this", THIS);
    keywords.put("true", TRUE);
    keywords.put("var", VAR);
    keywords.put("while", WHILE);
  }

  Scanner(String source) {
    this.source = source;
  }

  List<Token> scanTokens() {
    while (!isAtEnd()) {
      start = current;
      scanToken();
    }

    tokens.add(new Token(EOF, "", null, line));
    return tokens;
  }

  private boolean isAtEnd() {
    return current >= source.length();
  }

  /**
   * Starting with the current character, scan the next availible token
   */
  private void scanToken() {
    char c = advance();
    switch (c) {
    case '(':
      addToken(LEFT_PAREN);
      break;
    case ')':
      addToken(RIGHT_PAREN);
      break;
    case '{':
      addToken(LEFT_BRACE);
      break;
    case '}':
      addToken(RIGHT_BRACE);
      break;
    case ',':
      addToken(COMMA);
      break;
    case '.':
      addToken(DOT);
      break;
    case '-':
      addToken(MINUS);
      break;
    case '+':
      addToken(PLUS);
      break;
    case ';':
      addToken(SEMICOLON);
      break;
    case '*':
      addToken(STAR);
      break;

    case '!':
      addToken(match('=') ? BANG_EQUAL : BANG);
      break;
    case '=':
      addToken(match('=') ? EQUAL_EQUAL : EQUAL);
      break;
    case '<':
      addToken(match('=') ? LESS_EQUAL : LESS);
      break;
    case '>':
      addToken(match('=') ? GREATER_EQUAL : GREATER);
      break;

    case '/':
      if (match('/')) {
        // Comment detected: ignore rest of line
        while (peek() != '\n' && !isAtEnd())
          advance();
        // } else if (match('*')) {
        // multilineComment();
      } else {
        addToken(SLASH);
      }
      break;

    case ' ':
    case '\r':
    case '\t':
      break;

    case '\n':
      line++;
      break;

    case '"':
      string();
      break;

    default:
      if (isDigit(c)) {
        number();
      } else if (isAlpha(c)) {
        identifier();
      } else {
        // We keep scanning so later errors are also detected
        Lox.error(line, "Unexpected character.");
        break;
      }
    }
  }

  private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }

  private boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
  }

  private boolean isAlphaNumeric(char c) {
    return isAlpha(c) || isDigit(c);
  }

  /**
   * Scan an identifier and detect if it is a keyword
   */
  private void identifier() {
    while (isAlphaNumeric(peek()))
      advance();

    String text = source.substring(start, current);
    TokenType type = keywords.get(text);
    if (type == null)
      type = IDENTIFIER;
    addToken(type);
  }

  /**
   * Scan a number literal token
   */
  private void number() {
    while (isDigit(peek()))
      advance();

    if (peek() == '.' && isDigit(peekNext())) {
      advance();

      while (isDigit(peek()))
        advance();
    }

    addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
  }

  /**
   * Scan a string literal token
   */
  private void string() {
    while (peek() != '"' && !isAtEnd()) {
      if (peek() == '\n')
        line++;
      advance();
    }

    if (isAtEnd()) {
      Lox.error(line, "Unterminated string.");
      return;
    }

    advance();

    // We could also unescape any accepted escape sequences here
    String value = source.substring(start + 1, current - 1);
    addToken(STRING, value);
  }

  /**
   * Scan a mulit-line comment
   */
  // private void multilineComment() {
  // while(!(peek() == '*' && peekNext() == '/') && !isAtEnd()) {
  // if (peek() == '\n') line++;
  // else if (peek() == '/' && peekNext() == '*') {
  // advance();
  // multilineComment();
  // }
  // advance();
  // }

  // if (isAtEnd()) {
  // Lox.error(line, "Unterminated multi-line comment.");
  // }

  // advance();
  // advance();
  // }

  /**
   * Conditionally advance if next char == expected
   * 
   * @param expected
   * @return True if expected character was detected
   */
  private boolean match(char expected) {
    if (isAtEnd())
      return false;
    if (source.charAt(current) != expected)
      return false;

    current++;
    return true;
  }

  /**
   * Look ahead one character without advancing
   * 
   * @return The next character
   */
  private char peek() {
    if (isAtEnd())
      return '\0';
    return source.charAt(current);
  }

  /**
   * Look ahead 2 characters
   * 
   * @return The character after the next
   */
  private char peekNext() {
    if (current + 1 >= source.length())
      return '\0';
    return source.charAt(current + 1);
  }

  /**
   * Consume and return next character in source
   * 
   * @return The next character
   */
  private char advance() {
    return source.charAt(current++);
  }

  private void addToken(TokenType type) {
    addToken(type, null);
  }

  private void addToken(TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal, line));
  }

}
