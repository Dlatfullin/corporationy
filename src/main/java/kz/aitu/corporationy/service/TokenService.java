package kz.aitu.corporationy.service;

import kz.aitu.corporationy.dto.GeneratedToken;

public interface TokenService {

    GeneratedToken generate(String username);
}