package com.medilabo.front.dto;

import java.util.List;

public class PatientPageDTO {
    private List<PatientDTO> content;
    private int number;
    private int size;
    private boolean hasPrevious;
    private boolean hasNext;
}