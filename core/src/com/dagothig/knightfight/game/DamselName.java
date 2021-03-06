package com.dagothig.knightfight.game;

/**
* Created by dagothig on 4/18/15.
*/
public enum DamselName {
    Elizabeth,
    Alice,
    Mary,
    Joan,
    Agnes,
    Katherine,
    Margaret,
    Isabel,
    Margery,
    Anne,
    Jane,
    Catherine,
    Frances,
    Cecily,
    Millicent,
    Audrey,
    Elinor,
    Joyce,
    Frideswide,
    Rose,
    Helen,
    Bridget,
    Grace,
    Thomasin,
    Janet,
    Dorothy,
    Christian,
    Amy,
    Fortune,
    Sybil,
    Edith,
    Barbara,
    Gillian,
    Ursula,
    Susana,
    Emma,
    Rachel,
    Judith,
    Avis,
    Ellen,
    Lucy,
    Charity,
    Julian,
    Beatrice,
    Sarah,
    Martha,
    Mabel,
    Philippa,
    Blanche,
    Clemence,
    Marion,
    Ruth,
    Constance,
    Florence,
    Maria,
    Parnell,
    Wilmot,
    Denise,
    Josian,
    Maud,
    Rebecca,
    Christina,
    Lettice,
    Mildred,
    Winifred;

    public static DamselName getRandomName() {
        return values()[(int)(Math.random() * values().length)];
    }
}
