package ykvlv.blss.data.type;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RecipeTypeEnum {

    // СУПЫ
    SOUPS(null),
    HOT_SOUPS(SOUPS),
    COLD_SOUPS(SOUPS),
    BORSCH(HOT_SOUPS),
    SOLYANKA(HOT_SOUPS),
    OKROSHKA(COLD_SOUPS),

    // ГОРЯЧЕЕ
    HOT(null),
    HOT_MEAT(HOT),
    HOT_FISH(HOT),

    // САЛАТЫ
    SALADS(null),
    MEAT_SALADS(SALADS),

    // НАПИТКИ
    DRINKS(null),
    LEMONADE(DRINKS),
    COCKTAILS(DRINKS),

    // ДЕССЕРТЫ
    DESSERTS(null),
    CAKES(DESSERTS),
    SHORTBREAD(CAKES),
    WAFFLES(DESSERTS);

    private final RecipeTypeEnum parent;

}
