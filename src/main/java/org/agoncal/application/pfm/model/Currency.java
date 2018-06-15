package org.agoncal.application.pfm.model;

import java.io.Serializable;

public interface Currency extends Serializable {
    String getName();

    class Simple implements Currency {
        @Override
        public String getName() {
            return "SIMPLE";
        }
    }
}
