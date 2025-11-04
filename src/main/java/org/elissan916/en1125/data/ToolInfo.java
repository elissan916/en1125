package org.elissan916.en1125.data;

public record ToolInfo(String toolName, float dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge) {
    public ToolInfo(String toolName, float dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge) {
        if (toolName == null || toolName.isBlank()) {
            throw new IllegalArgumentException("Tool name cannot be null or blank");
        }

        if (dailyCharge < 0) {
            throw new IllegalArgumentException("Daily charge cannot be negative");
        }

        if (!weekdayCharge && !weekendCharge && !holidayCharge) {
            throw new IllegalArgumentException("At least one charge type must be true");
        }

        this.toolName = toolName;
        this.dailyCharge = dailyCharge;
        this.weekdayCharge = weekdayCharge;
        this.weekendCharge = weekendCharge;
        this.holidayCharge = holidayCharge;
    }
}