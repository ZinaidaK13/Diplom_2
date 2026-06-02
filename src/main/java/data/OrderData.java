package data;

import request.OrderRequest;

import java.util.Arrays;
import java.util.Collections;

public class OrderData {
    public static final String ORDER_CREATE_ENDPOINT = "/api/orders";

    public static final String BUN_CUCUMBER = "61c0c5a71d1f82001bdaaa71";
    public static final String INVALID_HASH = "invalid_hash_12345";

    public static OrderRequest createValidOrder() {
        return new OrderRequest(Arrays.asList(BUN_CUCUMBER, BUN_CUCUMBER));
    }

    public static OrderRequest createEmptyOrder() {
        return new OrderRequest(Collections.emptyList());
    }

    public static OrderRequest createInvalidOrder() {
        return new OrderRequest(Arrays.asList(INVALID_HASH, INVALID_HASH));
    }


}
