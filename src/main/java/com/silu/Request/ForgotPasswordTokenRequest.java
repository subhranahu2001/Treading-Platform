package com.silu.Request;

import com.silu.Domain.VerificationType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForgotPasswordTokenRequest {
    private String sendTo;

    private VerificationType verificationType;
}
