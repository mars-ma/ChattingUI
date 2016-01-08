package dev.mxw.widget;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Toast;
import dev.mxw.utils.EmojiUtil;

public class EmojiEditText extends EditText {
	// 输入表情前的光标位置
	private int cursorPos; // 输入表情前EditText中的文本
	private String inputAfterText; // 是否重置了EditText的内容
	private boolean resetText;
	private Context mContext;

	public EmojiEditText(Context context) {
		super(context);
		this.mContext = context;
		initEditText();
	}

	public EmojiEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initEditText();
	}

	public EmojiEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
		initEditText();
	} // 初始化edittext 控件

	private void initEditText() {
		addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int before, int count) {
				if (!resetText) {
					cursorPos = getSelectionEnd(); // 这里用s.toString()而不直接用s是因为如果用s，
					// 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
					// inputAfterText也就改变了，那么表情过滤就失败了
					inputAfterText = s.toString();
				}

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try {
					if (!resetText) {
						if (count > 0) {
							CharSequence input = s.subSequence(cursorPos, cursorPos + count);
							if (containsEmoji(getContext(), input.toString())) {
								resetText = true;
								// 暂不支持输入Emoji表情符号
								Toast.makeText(mContext, "不支持输入表情符号", Toast.LENGTH_SHORT).show(); // 是表情符号就将文本还原为输入表情符号之前的内容
								setText(inputAfterText);
								CharSequence text = getText();
								if (text instanceof Spannable) {
									Spannable spanText = (Spannable) text;
									Selection.setSelection(spanText, text.length());
								}
							}
						}
					} else {
						resetText = false;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});
	}

	/**
	 * 检测是否有emoji表情
	 * 
	 * @param source
	 * @return
	 */
	public static boolean containsEmoji(Context context, String str) {
		int unicode = Character.codePointAt(str, 0);
		int skip = Character.charCount(unicode);
		String tag = Integer.toHexString(unicode);

		if (EmojiUtil.isEmoji(context, tag)) {
			return true;
		} else {
			if (skip >= str.length()) {
				return false;
			}
			int followUnicode = Character.codePointAt(str, skip);	
			if (followUnicode == 0x20e3) {
				switch (unicode) {
				case 0x0031:
					return true;
				case 0x0032:
					return true;
				case 0x0033:
					return true;
				case 0x0034:
					return true;
				case 0x0035:
					return true;
				case 0x0036:
					return true;
				case 0x0037:
					return true;
				case 0x0038:
					return true;
				case 0x0039:
					return true;
				case 0x0030:
					return true;
				case 0x0023:
					return true;
				default:
					return false;
				}
			} else {
				switch (unicode) {
				case 0x1f1ef:
					return true;
				case 0x1f1fa:
					return true;
				case 0x1f1eb:
					return true;
				case 0x1f1e9:
					return true;
				case 0x1f1ee:
					return true;
				case 0x1f1ec:
					return true;
				case 0x1f1ea:
					return true;
				case 0x1f1f7:
					return true;
				case 0x1f1e8:
					return true;
				case 0x1f1f0:
					return true;
				default:
					return false;
				}
			}
		}
	}

}