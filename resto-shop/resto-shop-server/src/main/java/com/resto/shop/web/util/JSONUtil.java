package com.resto.shop.web.util;

import eleme.openapi.sdk.api.json.gson.*;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class JSONUtil {
	private static Gson _exploreGson;
	static {
		init();
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> parseJSONMessage(String json) {
		if (json == null || json.length() == 0) {
			return null;
		}
		Object obj = _exploreGson.fromJson(json, Object.class);
		return (Map<String, Object>) obj;
	}

	public static Map<String, Object> parseJSONMessageWithCheck(String json) {
		if (json == null || json.length() == 0) {
			return null;
		}

		try {
			Object obj = _exploreGson.fromJson(json, Object.class);
			return (Map<String, Object>) obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T parseJSONMessageWithCheck(String json, Class<T> cls) {
		if (json == null || json.length() == 0) {
			return null;
		}

		try {
			T obj = _exploreGson.fromJson(json, cls);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T parseJSONMessage(String json, Class<T> cls) {
		if (json == null || json.length() == 0) {
			return null;
		}
		return _exploreGson.fromJson(json, cls);
	}

	public static <T> T parseJSONMessage(String json, Type t) {
		if (json == null || json.length() == 0) {
			return null;
		}
		return _exploreGson.fromJson(json, t);
	}

	public static void init() {
		JsonSerializer<Time> ser = new JsonSerializer<Time>() {

			@Override
			public JsonElement serialize(Time src, Type typeOfSrc,
										 JsonSerializationContext context) {
				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
				JsonElement je = new JsonPrimitive(format.format(src));
				return je;
			}

		};

		JsonDeserializer<Time> deser = new JsonDeserializer<Time>() {

			@Override
			public Time deserialize(JsonElement json, Type typeOfT,
					JsonDeserializationContext context)
					throws JsonParseException {
				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
				Time t = new Time(0);
				try {
					t = new Time(format.parse(json.getAsString()).getTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return t;
			}

		};

		JsonSerializer<BigInteger> bsec = new JsonSerializer<BigInteger>() {

			@Override
			public JsonElement serialize(BigInteger src, Type typeOfSrc,
					JsonSerializationContext context) {
				JsonElement je = new JsonPrimitive("" + src.longValue());
				return je;
			}

		};

		JsonSerializer<Double> dsec = new JsonSerializer<Double>() {

			@Override
			public JsonElement serialize(Double src, Type typeOfSrc,
					JsonSerializationContext context) {
				JsonElement je = new JsonPrimitive("" + src.longValue());
				return je;
			}

		};

		JsonSerializer<Float> floatsec = new JsonSerializer<Float>() {

			@Override
			public JsonElement serialize(Float src, Type typeOfSrc,
					JsonSerializationContext context) {
				JsonElement je = new JsonPrimitive("" + src);
				return je;
			}

		};

		JsonSerializer<Integer> intsec = new JsonSerializer<Integer>() {

			@Override
			public JsonElement serialize(Integer src, Type typeOfSrc,
					JsonSerializationContext context) {
				JsonElement je = new JsonPrimitive("" + src);
				return je;
			}

		};

		JsonSerializer<Long> longsec = new JsonSerializer<Long>() {

			@Override
			public JsonElement serialize(Long src, Type typeOfSrc,
					JsonSerializationContext context) {
				JsonElement je = new JsonPrimitive("" + src);
				return je;
			}

		};
		GsonBuilder gsonB = new GsonBuilder();

		gsonB.registerTypeAdapter(Time.class, ser);
		gsonB.registerTypeAdapter(BigInteger.class, bsec);
		gsonB.registerTypeAdapter(Double.class, dsec);
		gsonB.registerTypeAdapter(Float.class, floatsec);
		gsonB.registerTypeAdapter(Integer.class, intsec);
		gsonB.registerTypeAdapter(Long.class, longsec);
		gsonB.registerTypeAdapter(Time.class, deser);
		gsonB.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		gsonB.disableHtmlEscaping();
		_exploreGson = gsonB.create();
	}

	public static String toJson(Object obj) {
		if (obj == null) {
			return null;
		}
		return _exploreGson.toJson(obj);
	}

	public static String toJsonWithNull(Object obj) {
		return _exploreGson.toJson(obj);
	}



}
