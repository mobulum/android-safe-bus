package io.vehiclehistory.safebus.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Base64;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import timber.log.BuildConfig;
import timber.log.Timber;

public class ObscuredSharedPreferences implements SharedPreferences {

    protected static final String UTF8 = "utf-8";
    private static final char[] SECRET = "sssssds".toCharArray();//BuildConfig.SHARED_PREFS_PASSWORD.toCharArray();

    protected SharedPreferences delegate;
    protected Context context;

    public ObscuredSharedPreferences(Context context, SharedPreferences delegate) {
        this.delegate = delegate;
        this.context = context;
    }

    public class Editor implements SharedPreferences.Editor {
        protected SharedPreferences.Editor delegate;

        @SuppressLint("CommitPrefEdits")
        public Editor() {
            this.delegate = ObscuredSharedPreferences.this.delegate.edit();
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            try {
                String encrypt = encrypt(Boolean.toString(value));
                delegate.putString(key, encrypt);
            } catch (ObscuredException e) {
                Timber.e(e, "putBoolean");
            }

            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            try {
                String encrypt = encrypt(Float.toString(value));
                delegate.putString(key, encrypt);
            } catch (ObscuredException e) {
                Timber.e(e, "putFloat");
            }

            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            try {
                String encrypt = encrypt(Integer.toString(value));
                delegate.putString(key, encrypt);
            } catch (ObscuredException e) {
                Timber.e(e, "putInt");
            }

            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            try {
                String encrypt = encrypt(Long.toString(value));
                delegate.putString(key, encrypt);
            } catch (ObscuredException e) {
                Timber.e(e, "putLong");
            }

            return this;
        }

        @Override
        public Editor putString(String key, String value) {
            try {
                String encrypt = encrypt(value);
                delegate.putString(key, encrypt);
            } catch (ObscuredException e) {
                Timber.e(e, "putString");
            }

            return this;
        }

        @Override
        public SharedPreferences.Editor putStringSet(String s, Set<String> v) {
            if (v != null && !v.isEmpty()) {
                Set<String> encrypted = new HashSet<>(v.size());
                for (String val : v) {
                    try {
                        String encrypt = encrypt(val);
                        encrypted.add(encrypt);
                    } catch (ObscuredException e) {
                        Timber.e(e, "putStringSet");
                    }

                }
                delegate.putStringSet(s, encrypted);
            }

            return this;
        }

        @Override
        public void apply() {
            delegate.apply();
        }

        @Override
        public Editor clear() {
            delegate.clear();
            return this;
        }

        @Override
        public boolean commit() {
            return delegate.commit();
        }

        @Override
        public Editor remove(String s) {
            delegate.remove(s);
            return this;
        }
    }

    public Editor edit() {
        return new Editor();
    }


    @Override
    public Map<String, ?> getAll() {
        throw new UnsupportedOperationException(); // left as an exercise to the reader
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        final String v = delegate.getString(key, null);
        if (v != null) {
            try {
                String decrypt = decrypt(v);
                return Boolean.parseBoolean(decrypt);
            } catch (ObscuredException e) {
                Timber.e(e, "getBoolean");
            }
        }
        return defValue;
    }

    @Override
    public float getFloat(String key, float defValue) {
        final String v = delegate.getString(key, null);
        if (v != null) {
            try {
                String decrypt = decrypt(v);
                return Float.parseFloat(decrypt);
            } catch (ObscuredException e) {
                Timber.e(e, "getFloat");
            }
        }
        return defValue;
    }

    @Override
    public int getInt(String key, int defValue) {
        final String v = delegate.getString(key, null);
        if (v != null) {
            try {
                String decrypt = decrypt(v);
                return Integer.parseInt(decrypt);
            } catch (ObscuredException e) {
                Timber.e(e, "getInt");
            }
        }
        return defValue;
    }

    @Override
    public long getLong(String key, long defValue) {
        final String v = delegate.getString(key, null);
        if (v != null) {
            try {
                String decrypt = decrypt(v);
                return Long.parseLong(decrypt);
            } catch (ObscuredException e) {
                Timber.e(e, "getLong");
            }
        }
        return defValue;
    }

    @Override
    public String getString(String key, String defValue) {
        final String v = delegate.getString(key, null);
        if (v != null) {
            try {
                return decrypt(v);
            } catch (ObscuredException e) {
                Timber.e(e, "getString");
            }
        }
        return defValue;
    }

    @Override
    public Set<String> getStringSet(String s, Set<String> defValue) {
        final Set<String> v = delegate.getStringSet(s, null);

        if (v != null && !v.isEmpty()) {
            Set<String> decrypted = new HashSet<>(v.size());
            for (String val : v) {
                try {
                    decrypted.add(decrypt(val));
                } catch (ObscuredException e) {
                    Timber.e(e, "getStringSet");
                }
            }
            return decrypted;
        }
        return defValue;
    }

    @Override
    public boolean contains(String s) {
        return delegate.contains(s);
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        delegate.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        delegate.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }


    protected String encrypt(String value) throws ObscuredException {

        try {
            final byte[] bytes = value != null ? value.getBytes(UTF8) : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SECRET));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(Settings.Secure.getString(context.getContentResolver(), Settings.System.ANDROID_ID).getBytes(UTF8), 20));
            return new String(Base64.encode(pbeCipher.doFinal(bytes), Base64.NO_WRAP), UTF8);

        } catch (Exception e) {
            Timber.e(e, "encrypt failed");
            throw new ObscuredException(e);
        }

    }

    protected String decrypt(String value) throws ObscuredException {
        try {
            final byte[] bytes = value != null ? Base64.decode(value, Base64.DEFAULT) : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SECRET));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(Settings.Secure.getString(context.getContentResolver(), Settings.System.ANDROID_ID).getBytes(UTF8), 20));
            return new String(pbeCipher.doFinal(bytes), UTF8);

        } catch (Exception e) {
            Timber.e(e, "decrypt failed");
            throw new ObscuredException(e);
        }
    }

}