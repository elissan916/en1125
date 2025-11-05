package org.elissan916.en1125.data;

/**
 * Data holder describing pricing/charging rules for a tool type.
 *
 * @param toolName human-readable tool type name (used as the key in maps),
 *                 same identifier used in Tool data type
 * @param dailyCharge per-day charge for the tool type
 * @param weekdayCharge whether weekdays are charged
 * @param weekendCharge whether weekends are charged
 * @param holidayCharge whether holidays are charged
 */
public record ToolInfo(String toolName, float dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge) {
    public ToolInfo {
        if (toolName == null || toolName.isBlank()) {
            throw new IllegalArgumentException("Tool name cannot be null or blank");
        }

        if (dailyCharge < 0) {
            throw new IllegalArgumentException("Daily charge cannot be negative");
        }

        if (!weekdayCharge && !weekendCharge && !holidayCharge) {
            throw new IllegalArgumentException("At least one charge type must be true");
        }
    }
}