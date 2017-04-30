package com.petbooking.API.Pet;

import com.petbooking.Constants.APIConstants;
import com.petbooking.Constants.AppConstants;

/**
 * Created by Luciano José on 30/04/2017.
 */

public class APIPetConstants {

    public static final String DATA_PETS = "pets";
    public static final String DATA_BASE64 = "data:image/jpeg;base64,";
    public static final String DATA_GENDER_DOG = "dog";
    public static final String DATA_GENDER_CAT = "cat";
    public static final String DATA_GENDER_MALE = "male";
    public static final String DATA_GENDER_FEMALE = "female";
    public static final String DATA_SIZE_SMALL = "small";
    public static final String DATA_SIZE_MEDIUM = "medium";
    public static final String DATA_SIZE_BIG = "big";
    public static final String DATA_SIZE_GIANT = "giant";
    public static final String DATA_TYPE_SHORT = "short_coat";
    public static final String DATA_TYPE_MEDIUM = "medium_coat";
    public static final String DATA_TYPE_LONG = "long_coat";

    public static final String PATH_PET_ID = "petID";

    /* Query */
    public static final String QUERY_PAGE_SIZE_DEAFULT = "?page[size]=250";

    /**
     * APIs
     */
    public static final String ENDPOINT_LIST_BREEDS_CAT = "breeds/cat" + QUERY_PAGE_SIZE_DEAFULT;

    public static final String ENDPOINT_LIST_BREEDS_DOG = "breeds/dog" + QUERY_PAGE_SIZE_DEAFULT;

    public static final String ENDPOINT_LIST_PETS = "users/{" + APIConstants.PATH_PARAM + "}/pets";

    public static final String ENDPOINT_CREATE_USER = ENDPOINT_LIST_PETS;

    public static final String ENDPOINT_UPDATE_PET = ENDPOINT_LIST_PETS + "/{" + PATH_PET_ID + "}";

    public static final String ENDPOINT_REMOVE_PET = ENDPOINT_UPDATE_PET;
}
