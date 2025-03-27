package ru.maslennikov.thirdProject.SensorApp.dto;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


    public class SensorDto {

        public SensorDto() {}

        @NotEmpty(message = "Sensor name cannot be null")
        @Size(min = 3, max = 30,message = "Size should be between 3 and 30 characters")
        private String name;

        public @Size(min = 3, max = 30, message = "Size should be between 3 and 30 characters") String getName() {
            return name;
        }

        public void setName(@Size(min = 3, max = 30, message = "Size should be between 3 and 30 characters") String name) {
            this.name = name;
        }
    }
