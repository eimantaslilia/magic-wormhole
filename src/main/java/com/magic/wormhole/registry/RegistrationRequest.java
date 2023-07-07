package com.magic.wormhole.registry;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record RegistrationRequest(@NotBlank String name, @NotNull int port){
}
