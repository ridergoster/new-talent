package net.andoria.newtalent.network;

import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;

/**
 * Created by maxime on 09/07/16.
 */
public enum ResponseStatus {
    OK,
    KO;

    public static class EnumConverter extends StringBasedTypeConverter<ResponseStatus> {
        @Override
        public ResponseStatus getFromString(String s) {
            return ResponseStatus.valueOf(s);
        }

        @Override
        public String convertToString(ResponseStatus object) {
            return object.toString();
        }
    }
}
