package com.magic.wormhole;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record RegistrationRequest(@NotBlank String name, @NotNull int port){
}
