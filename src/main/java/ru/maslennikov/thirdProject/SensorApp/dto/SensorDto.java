package ru.maslennikov.thirdProject.SensorApp.dto;

import javax.validation.constraints.Size;


    public class SensorDto {

        public SensorDto() {}

        @Size(min = 3, max = 30,message = "Size should be between 3 and 30 characters")
        private String name;

        public @Size(min = 3, max = 30, message = "Size should be between 3 and 30 characters") String getName() {
            return name;
        }

        public void setName(@Size(min = 3, max = 30, message = "Size should be between 3 and 30 characters") String name) {
            this.name = name;
        }
    }
