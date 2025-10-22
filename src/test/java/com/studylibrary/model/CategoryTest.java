package com.studylibrary.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for the Category class.
 * Tests category creation, properties, equality, and string representation.
 */
@DisplayName("Category Tests")
class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
    }

    @Test
    @DisplayName("Should create category with default constructor and generate UUID")
    void testDefaultConstructor() {
        assertThat(category.getId())
                .isNotNull()
                .matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        assertThat(category.getColor()).isEqualTo("#3498db"); // Default blue
        assertThat(category.getName()).isNull();
        assertThat(category.getDescription()).isNull();
    }

    @Test
    @DisplayName("Should create category with name constructor")
    void testNameConstructor() {
        Category namedCategory = new Category("Programming");

        assertThat(namedCategory.getId()).isNotNull();
        assertThat(namedCategory.getName()).isEqualTo("Programming");
        assertThat(namedCategory.getColor()).isEqualTo("#3498db"); // Default blue
    }

    @Test
    @DisplayName("Should create category with name and color constructor")
    void testNameAndColorConstructor() {
        Category coloredCategory = new Category("Science", "#e74c3c");

        assertThat(coloredCategory.getId()).isNotNull();
        assertThat(coloredCategory.getName()).isEqualTo("Science");
        assertThat(coloredCategory.getColor()).isEqualTo("#e74c3c");
    }

    @Test
    @DisplayName("Should set and get name")
    void testNameProperty() {
        category.setName("Mathematics");

        assertThat(category.getName()).isEqualTo("Mathematics");
    }

    @Test
    @DisplayName("Should set and get color")
    void testColorProperty() {
        category.setColor("#2ecc71");

        assertThat(category.getColor()).isEqualTo("#2ecc71");
    }

    @Test
    @DisplayName("Should set and get description")
    void testDescriptionProperty() {
        category.setDescription("All programming-related materials");

        assertThat(category.getDescription()).isEqualTo("All programming-related materials");
    }

    @Test
    @DisplayName("Should set and get ID")
    void testIdProperty() {
        String customId = "custom-id-123";
        category.setId(customId);

        assertThat(category.getId()).isEqualTo(customId);
    }

    @Test
    @DisplayName("Should handle null name gracefully")
    void testNullName() {
        category.setName(null);

        assertThat(category.getName()).isNull();
        assertThat(category.toString()).isEqualTo("Unnamed Category");
    }

    @Test
    @DisplayName("Should handle null color")
    void testNullColor() {
        category.setColor(null);

        assertThat(category.getColor()).isNull();
    }

    @Test
    @DisplayName("Should handle null description")
    void testNullDescription() {
        category.setDescription(null);

        assertThat(category.getDescription()).isNull();
    }

    @Test
    @DisplayName("Should return name in toString when name is set")
    void testToStringWithName() {
        category.setName("History");

        assertThat(category.toString()).isEqualTo("History");
    }

    @Test
    @DisplayName("Should return 'Unnamed Category' in toString when name is null")
    void testToStringWithoutName() {
        assertThat(category.toString()).isEqualTo("Unnamed Category");
    }

    @Test
    @DisplayName("Should be equal when IDs match")
    void testEqualityWithSameId() {
        Category category1 = new Category("Category1");
        Category category2 = new Category("Category2");
        category2.setId(category1.getId()); // Same ID

        assertThat(category1).isEqualTo(category2);
        assertThat(category1.hashCode()).isEqualTo(category2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when IDs differ")
    void testEqualityWithDifferentId() {
        Category category1 = new Category("Category1");
        Category category2 = new Category("Category1"); // Same name, different ID

        assertThat(category1).isNotEqualTo(category2);
        assertThat(category1.hashCode()).isNotEqualTo(category2.hashCode());
    }

    @Test
    @DisplayName("Should be equal to itself")
    void testEqualityWithSelf() {
        assertThat(category).isEqualTo(category);
    }

    @Test
    @DisplayName("Should not be equal to null")
    void testEqualityWithNull() {
        assertThat(category).isNotEqualTo(null);
    }

    @Test
    @DisplayName("Should not be equal to different class")
    void testEqualityWithDifferentClass() {
        String notACategory = "Not a category";

        assertThat(category).isNotEqualTo(notACategory);
    }

    @Test
    @DisplayName("Should generate consistent hashCode")
    void testHashCodeConsistency() {
        int initialHashCode = category.hashCode();

        // Change properties (but not ID)
        category.setName("Test");
        category.setColor("#fff");
        category.setDescription("Description");

        assertThat(category.hashCode()).isEqualTo(initialHashCode);
    }

    @Test
    @DisplayName("Should handle empty string name")
    void testEmptyStringName() {
        category.setName("");

        assertThat(category.getName()).isEmpty();
        assertThat(category.toString()).isEmpty();
    }

    @Test
    @DisplayName("Should handle whitespace-only name")
    void testWhitespaceName() {
        category.setName("   ");

        assertThat(category.getName()).isEqualTo("   ");
        assertThat(category.toString()).isEqualTo("   ");
    }

    @Test
    @DisplayName("Should accept valid hex color codes")
    void testValidHexColorCodes() {
        category.setColor("#FF5733");
        assertThat(category.getColor()).isEqualTo("#FF5733");

        category.setColor("#abc");
        assertThat(category.getColor()).isEqualTo("#abc");

        category.setColor("#123456");
        assertThat(category.getColor()).isEqualTo("#123456");
    }

    @Test
    @DisplayName("Should handle long description")
    void testLongDescription() {
        String longDescription = "This is a very long description that contains a lot of text " +
                "to describe the category in great detail with multiple sentences and clauses.";

        category.setDescription(longDescription);

        assertThat(category.getDescription()).isEqualTo(longDescription);
    }

    @Test
    @DisplayName("Should maintain property independence")
    void testPropertyIndependence() {
        category.setName("Test");
        category.setColor("#000");
        category.setDescription("Desc");

        Category anotherCategory = new Category();

        // Verify the other category has default/null values
        assertThat(anotherCategory.getName()).isNull();
        assertThat(anotherCategory.getColor()).isEqualTo("#3498db");
        assertThat(anotherCategory.getDescription()).isNull();
    }

    @Test
    @DisplayName("Should generate unique IDs for different instances")
    void testUniqueIdGeneration() {
        Category cat1 = new Category();
        Category cat2 = new Category();
        Category cat3 = new Category();

        assertThat(cat1.getId())
                .isNotEqualTo(cat2.getId())
                .isNotEqualTo(cat3.getId());
        assertThat(cat2.getId()).isNotEqualTo(cat3.getId());
    }
}
