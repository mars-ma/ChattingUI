package dev.mxw.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.text.TextUtils;

public class EmojiUtil {

	public static ArrayList<String> codes;

	/**
	 * 
	 * @param source
	 * @return
	 */
	public static String filterEmoji(Context context, String str) {

		StringBuilder buf = null;
		if (buf == null) {
			buf = new StringBuilder();
		}
		int len = str.length();
		int skip = 0;

		for (int i = 0; i < len; i = i + skip) {
			int unicode = Character.codePointAt(str, i);
			char codePoint = str.charAt(i);
			skip = Character.charCount(unicode);
//			Logger.e("emoji", "unicode " + Integer.toHexString(unicode));
//			Logger.e("emoji", "skip " + skip);

			String tag = Integer.toHexString(unicode);
			if (isEmoji(context, tag)) {
				buf.append("[em]" + Integer.toHexString(unicode) + "[/em]");
			} else {
				if (i + skip >= len) {
					buf.append(codePoint);
				} else {
					int followUnicode = Character.codePointAt(str, i + skip);
					if (followUnicode == 0x20e3) {
						String emoji = "-20e3";
						int followSkip = Character.charCount(followUnicode);
						switch (unicode) {
						case 0x0031:
							emoji = "0031" + emoji;
							break;
						case 0x0032:
							emoji = "0032" + emoji;
							break;
						case 0x0033:
							emoji = "0033" + emoji;
							break;
						case 0x0034:
							emoji = "0034" + emoji;
							break;
						case 0x0035:
							emoji = "0035" + emoji;
							break;
						case 0x0036:
							emoji = "0036" + emoji;
							break;
						case 0x0037:
							emoji = "0037" + emoji;
							break;
						case 0x0038:
							emoji = "0038" + emoji;
							break;
						case 0x0039:
							emoji = "0039" + emoji;
							break;
						case 0x0030:
							emoji = "0030" + emoji;
							break;
						case 0x0023:
							emoji = "0031" + emoji;
							break;
						default:
							emoji = "";
							followSkip = 0;
							break;
						}
						if (!TextUtils.isEmpty(emoji)
								&& isEmoji(context, emoji)) {
							buf.append("[em]" + emoji + "[/em]");
						} else {
							buf.append(codePoint);
						}
						skip += followSkip;
					} else {
						String emoji = Integer.toHexString(unicode) + "-";
						int followSkip = Character.charCount(followUnicode);
						switch (unicode) {
						case 0x1f1ef:
							emoji = emoji + Integer.toHexString(followUnicode);
							break;
						case 0x1f1fa:
							emoji = emoji + Integer.toHexString(followUnicode);
							break;
						case 0x1f1eb:
							emoji = emoji + Integer.toHexString(followUnicode);
							break;
						case 0x1f1e9:
							emoji = emoji + Integer.toHexString(followUnicode);
							break;
						case 0x1f1ee:
							emoji = emoji + Integer.toHexString(followUnicode);
							break;
						case 0x1f1ec:
							emoji = emoji + Integer.toHexString(followUnicode);
							break;
						case 0x1f1ea:
							emoji = emoji + Integer.toHexString(followUnicode);
							break;
						case 0x1f1f7:
							emoji = emoji + Integer.toHexString(followUnicode);
							break;
						case 0x1f1e8:
							emoji = emoji + Integer.toHexString(followUnicode);
							break;
						case 0x1f1f0:
							emoji = emoji + Integer.toHexString(followUnicode);
							break;
						default:
							emoji = "";
							followSkip = 0;
							break;
						}
						if (!TextUtils.isEmpty(emoji)
								&& isEmoji(context, emoji)) {
							buf.append("[em]" + emoji + "[/em]");
						} else {
							buf.append(codePoint);
						}
						skip += followSkip;
					}
				}
			}
		}

		return buf.toString();
	}

	public static boolean isEmoji(Context context, String tag) {
		// Log.e("emoji", "is emoji " + tag);
		if (codes == null) {
			InputStream in = null;
			try {
				in = context.getResources().getAssets().open("unicodes.xml");
			} catch (IOException e) {
				e.printStackTrace();
			}
			codes = getUnicodes(in);
		}

		return codes.contains(tag);
	}

	public static ArrayList<String> getUnicodes(InputStream in) {
		ArrayList<String> unicodes = new ArrayList<String>();
		XmlPullParserFactory factory;
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(in, "UTF-8");
			int evtType = xpp.getEventType();
			while (evtType != XmlPullParser.END_DOCUMENT) {
				switch (evtType) {
				case XmlPullParser.START_TAG:
					String tag = xpp.getName();
					if ("code".equals(tag)) {
						String code_value = xpp.nextText();
						unicodes.add(code_value);
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				default:
					break;
				}
				evtType = xpp.next();
			}
			return unicodes;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getEmoji(Context context, String text) {
		Pattern p = Pattern.compile("(?i)\\[em\\](.*?)\\[/em\\]");// ƥ��[emoji]��ͷ��[/emoji]��β���ĵ�
		Matcher m = p.matcher(text);// ��ʼ����
		while (m.find()) {
			String emojinamegourp = m.group(0);
			String emojiname = m.group(1);
			try {
				char emoji[] = Character.toChars(Integer.valueOf(emojiname, 16)
						.intValue());
				int start = text.indexOf(emojinamegourp);
				StringBuffer sb0 = new StringBuffer(text);
				sb0 = sb0.replace(start, start + emojinamegourp.length(),
						new String(emoji));
				text = sb0.toString();
			} catch (Exception e) {
				if (isEmoji(context, emojiname)) {
					if (emojiname.contains("-")) {
						String begin = emojiname.substring(0,
								emojiname.indexOf("-"));
						String end = emojiname.substring(
								emojiname.indexOf("-") + 1, emojiname.length());
						Logger.e("emoji", "f " + begin);
						Logger.e("emoji", "e " + end);

						char first[] = Character.toChars(Integer.valueOf(begin,
								16).intValue());
						char last[] = Character.toChars(Integer
								.valueOf(end, 16).intValue());

						StringBuffer sb = new StringBuffer();
						sb.append(first);
						sb.append(last);
						Logger.e("emoji", "sb " + sb.toString());

						int start = text.indexOf(emojinamegourp);
						StringBuffer sb0 = new StringBuffer(text);
						sb0 = sb0.replace(start,
								start + emojinamegourp.length(), sb.toString());
						text = sb0.toString();

					} else {
						int start = text.indexOf(emojinamegourp);
						StringBuffer sb0 = new StringBuffer(text);
						sb0 = sb0.replace(start,
								start + emojinamegourp.length(), "");
						text = sb0.toString();
					}
				}
				// e.printStackTrace();
			}
		}
		return text;
	}
}