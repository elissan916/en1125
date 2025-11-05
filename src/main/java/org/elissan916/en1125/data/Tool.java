package org.elissan916.en1125.data;

/**
 * Simple record that maps a tool code (short identifier) to its type and brand.
 *
 * @param toolCode unique short code used in checkouts (e.g. "JAKR"),
 *                 used as the key in Maaps of this type
 * @param toolName human-friendly tool type name (e.g. "Jackhammer"),
 *                 same identifier used in ToolInfo data structure
 * @param brand manufacturer/brand name
 */
public record Tool(String toolCode, String toolName, String brand) {
    public Tool {
        if (toolCode == null || toolCode.isBlank()) {
            throw new IllegalArgumentException("Tool code cannot be null or blank");
        }

        if (toolName == null || toolName.isBlank()) {
            throw new IllegalArgumentException("Tool type cannot be null or blank");
        }

        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("Brand cannot be null or blank");
        }
    }
}
