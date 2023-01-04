package com.petspace.dev.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncode passwordEncode;
    private static final String ADMIN_TOKEN = "LzFtjJ7gKM!6k3FF2PJ6yXs2SnU4zd$6Nn$e&!t";


}
