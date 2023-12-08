package com.dicoding.scancare.data

data class Ingredient(
    val id: Int,
    val name: String,
    val function: String
)

object DummyData {
    val ingredientsList: List<Ingredient>
        get() = listOf(
            Ingredient(
                1,
                "Alpha Hydroxy Acids",
                "Alpha hydroxy acids (AHAs) are a group of natural acids found in foods. These acids, most commonly derived from fruits and milk, have incredible exfoliating properties. They work by loosening the glue-like substance that holds dull and dead skin cells on the topmost layer of your skin, encouraging cell turnover and revealing newer, brighter skin underneath."
            ),
            Ingredient(
                2,
                "Retinoids",
                "Retinoids are derivatives of vitamin A and are one of the most researched skincare ingredients. They work wonders for your skin by boosting collagen production, reducing fine lines, wrinkles, and hyperpigmentation. Retinoids accelerate cell turnover, preventing dead skin cells from clogging pores, and enhancing the overall texture of your skin."
            ),
            Ingredient(
                3,
                "Hyaluronic Acid",
                "Hyaluronic acid is a moisture-binding ingredient that is naturally present in our skin. It has a remarkable ability to attract and retain moisture, making it a great hydrating agent. It plumps up the skin, diminishes the appearance of fine lines and wrinkles, and gives a supple, dewy complexion."
            ),
            Ingredient(
                4,
                "Niacinamide",
                "Niacinamide, also known as vitamin B3, is a versatile ingredient that offers multiple benefits to the skin. It helps in reducing redness, strengthens the skin's barrier, regulates sebum production, and minimizes the appearance of pores. Niacinamide also has anti-inflammatory properties, making it suitable for various skin types."
            ),
            Ingredient(
                5,
                "Vitamin C",
                "Vitamin C is a potent antioxidant that brightens the skin, reduces dark spots, and boosts collagen production. It shields the skin from environmental stressors like pollution and UV rays, evens out skin tone, and promotes a radiant complexion."
            ),
            Ingredient(
                6,
                "Salicylic Acid",
                "Salicylic acid is a beta-hydroxy acid (BHA) that penetrates deep into the pores, unclogging them and preventing acne breakouts. It exfoliates the skin, removes dead skin cells, and effectively treats acne, blackheads, and whiteheads."
            ),
            Ingredient(
                7,
                "Glycolic Acid",
                "Glycolic acid is an alpha-hydroxy acid (AHA) derived from sugarcane. It exfoliates the skin, improves texture, treats hyperpigmentation, and reduces the appearance of fine lines and wrinkles. Glycolic acid enhances cell turnover, revealing smoother, brighter skin."
            ),
            Ingredient(
                8,
                "Ceramides",
                "Ceramides are lipids that form a protective barrier in the outer layer of the skin. They help retain moisture, prevent dryness, and strengthen the skin's natural barrier function. Ceramides play a crucial role in maintaining healthy and hydrated skin."
            ),
            Ingredient(
                9,
                "Peptides",
                "Peptides are small chains of amino acids that are the building blocks of proteins in the skin. They have various functions, such as boosting collagen production, reducing fine lines and wrinkles, and improving skin elasticity and firmness."
            ),
            Ingredient(
                10,
                "Benzoyl Peroxide",
                "Benzoyl peroxide is an effective ingredient for treating acne. It kills acne-causing bacteria, reduces inflammation, unclogs pores, and helps prevent future breakouts. Benzoyl peroxide is available in different strengths and formulations for acne treatment."
            )
            // Tambahkan data lain sesuai kebutuhan
        )
}