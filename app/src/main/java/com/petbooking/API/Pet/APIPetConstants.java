package com.petbooking.API.Pet;

import com.petbooking.Constants.APIConstants;

/**
 * Created by Luciano José on 30/04/2017.
 */

public class APIPetConstants {

    public static final String DATA_PETS = "pets";
    public static final String DATA_BASE64 = "data:image/jpeg;base64,";
    public static final String DATA_TYPE_DOG = "dog";
    public static final String DATA_TYPE_CAT = "cat";
    public static final String DATA_GENDER_MALE = "male";
    public static final String DATA_GENDER_FEMALE = "female";
    public static final String DATA_SIZE_SMALL = "small";
    public static final String DATA_SIZE_MEDIUM = "medium";
    public static final String DATA_SIZE_BIG = "big";
    public static final String DATA_SIZE_GIANT = "giant";
    public static final String DATA_TYPE_SHORT = "short_coat";
    public static final String DATA_TYPE_MEDIUM = "medium_coat";
    public static final String DATA_TYPE_LONG = "long_coat";
    public static final String DATA_TEMPER_AGITATED = "agitated";
    public static final String DATA_TEMPER_HAPPY = "happy";
    public static final String DATA_TEMPER_LOVELY = "lovely";
    public static final String DATA_TEMPER_ANGRY = "angry";
    public static final String DATA_TEMPER_PLAYFUL = "playful";
    public static final String DATA_TEMPER_NEEDY = "needy";
    public static final String DATA_TEMPER_AFFECTIONATE = "affectionate";
    public static final String DATA_TEMPER_DOCILE = "docile";
    public static final String DATA_TEMPER_QUIET = "quiet";
    public static final String DATA_TEMPER_BRAVE = "brave";

    public static final String PATH_PET_ID = "petID";

    /* Query */
    public static final String QUERY_PAGE_SIZE_DEAFULT = "?page[size]=250";

    /**
     * APIs
     */
    public static final String LIST_BREEDS_CAT_ENDPOINT = "breeds/cat" + QUERY_PAGE_SIZE_DEAFULT;

    public static final String LIST_BREEDS_DOG_ENDPOINT = "breeds/dog" + QUERY_PAGE_SIZE_DEAFULT;

    public static final String LIST_PETS_ENDPOINT = "users/{" + APIConstants.PATH_PARAM + "}/pets";

    public static final String CREATE_USER_ENDPOINT = LIST_PETS_ENDPOINT;

    public static final String UPDATE_PET_ENDPOINT = LIST_PETS_ENDPOINT + "/{" + PATH_PET_ID + "}";

    public static final String REMOVE_PET_ENDPOINT = UPDATE_PET_ENDPOINT;

    public static final  String GET_PET_ATRIBUTTES = "users/{" + APIConstants.PATH_PARAM + "}/pets/dog/options";
}
