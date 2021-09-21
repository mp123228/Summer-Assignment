package com.example.g_summerassignmnet.Testbooth_location;

import java.util.List;

public class Result {

    private List<String> destination_addresses;

    private List<String> origin_addresses;
    private List<Rows> rows;

    String status;
    String mode;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<String> getDestination_address() {
        return destination_addresses;
    }

    public void setDestination_address(List<String> destination_address)
    {
        this.destination_addresses = destination_address;
    }

    public List<String> getOrigin_addresses() {
        return origin_addresses;
    }

    public void setOrigin_addresses(List<String> origin_addresses) {
        this.origin_addresses = origin_addresses;
    }

    public List<Rows> getRows() {
        return rows;
    }

    public void setRows(List<Rows> rows) {
        this.rows = rows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public class Distance{

        private String text;
        private int  value;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String toString()
        {
            return "Duration{" +
                    "text='" + text + '\'' +
                    ", value=" + value +
                    '}';
        }
    }
    class Duration
    {
        private String text;
        private int  value;

        public String getText()
        {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Duration{" +
                    "text='" + text + '\'' +
                    ", value=" + value +
                    '}';
        }
    }

    public class Elements
    {
        private Distance distance;

        private Duration duration;

        private String status;

        public Distance getDistance() {
            return distance;
        }

        public void setDistance(Distance distance) {
            this.distance = distance;
        }

        public Duration getDuration() {
            return duration;
        }

        public void setDuration(Duration duration) {
            this.duration = duration;
        }

        public String getStatus()
        {
            return status;
        }

        public void setStatus(String status)
        {
            this.status = status;
        }

        @Override
        public String toString()
        {
            return "Elements{" +
                    "Distance=" + distance +
                    ", duration=" + duration +
                    ", status='" + status + '\'' +
                    '}';
        }

    }

    public class Rows
    {
        private List<Elements> elements;

        public List<Elements> getElements() {
            return elements;
        }

        public void setElements(List<Elements> elements) {
            this.elements = elements;
        }

        @Override
        public String toString() {
            return "Rows{" +
                    "elements=" + elements +
                    '}';
        }
    }

    @Override
    public String toString()
    {
        return "Result{" +
                "destination_address=" + destination_addresses +
                ", origian_addresses=" + origin_addresses +
                ", rows=" + rows +
                ", status='" + status + '\'' +
                ", mode='" + mode + '\'' +
                '}';

    }
}
