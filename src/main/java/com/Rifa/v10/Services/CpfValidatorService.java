package com.Rifa.v10.Services;


import org.springframework.stereotype.Service;

@Service
public class CpfValidatorService {

    public boolean isValid(String cpf) {
        if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d+")) {
            return false;
        }

        return isCpfValid(cpf);
    }

    private boolean isCpfValid(String cpf) {
        int[] digits = new int[11];
        for (int i = 0; i < 11; i++) {
            digits[i] = Character.getNumericValue(cpf.charAt(i));
        }

        return (calculateDigit(digits, 10) == digits[9] && calculateDigit(digits, 11) == digits[10]);
    }

    private int calculateDigit(int[] digits, int factor) {
        int sum = 0;
        for (int i = 0; i < factor - 1; i++) {
            sum += digits[i] * (factor - i);
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}