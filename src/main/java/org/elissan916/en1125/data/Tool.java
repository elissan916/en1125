package org.elissan916.en1125.data;

public record Tool(String toolCode, String toolName, String brand) {
    public Tool(String toolCode, String toolName, String brand) {
        if (toolCode == null || toolCode.isBlank()) {
            throw new IllegalArgumentException("Tool code cannot be null or blank");
        }

        if (toolName == null || toolName.isBlank()) {
            throw new IllegalArgumentException("Tool type cannot be null or blank");
        }

        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("Brand cannot be null or blank");
        }

        this.toolCode = toolCode;
        this.toolName = toolName;
        this.brand = brand;
    }
}
