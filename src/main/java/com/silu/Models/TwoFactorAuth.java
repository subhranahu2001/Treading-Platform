package com.silu.Models;

import com.silu.Domain.VerificationType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwoFactorAuth {

    private boolean isEnabled = false;

    private VerificationType sendTo;
}
