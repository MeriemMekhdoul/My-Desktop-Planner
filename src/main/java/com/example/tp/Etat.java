package com.example.tp;

import java.io.Serializable;

public enum Etat implements Serializable {
    NOT_REALIZED,
    COMPLETED,
    IN_PROGRESS,
    CANCELLED,
    DELAYED
}
