package pro.inmost.amazon.chime.service;

import pro.inmost.amazon.chime.model.dto.LoginRequestDTO;

public interface LoginService {

    <T extends Object> T login(LoginRequestDTO loginRequestDTO);

}
