package com.example.android.nfc.discover;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StringBuilder sb = new StringBuilder();
		Intent intent = getIntent();
		// IDm を取得
		byte[] idm = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
		sb.append("IDm: ");
		for (byte b : idm) {
			sb.append(String.format("%02X", b));
		}
		sb.append("\n");
		// Tag から情報を取得
		Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		if (tag != null) {
			getInfo(tag, sb);
		}
		// NdefMessage から情報を取得
		NdefMessage msg = (NdefMessage) intent.getParcelableExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		if (msg != null) {
			getInfo(msg, sb);
		}
		((TextView) findViewById(R.id.textView1)).setText(new String(sb));
	}
	
	private void getInfo(Tag tag, StringBuilder sb) {
		String[] techList = tag.getTechList();
		for (String tech : techList) {
			sb.append(" " + tech + "\n");
		}
	}
	
	private void getInfo(NdefMessage msg, StringBuilder sb) {
		byte[] msgBytes = msg.toByteArray();
		if (msgBytes != null) {
			sb.append("Message: ");
			for (byte b : msgBytes) {
				// NDEF 1.0 specification でフォーマットされる
				sb.append(String.format("%02X", b));
			}
			sb.append("\n");
		}
		
		NdefRecord[] records = msg.getRecords();
		for (int i = 0; i < records.length; i++) {
			sb.append("Message" + i + ":");
			sb.append(" id: ");
			for (byte b : records[i].getId()) {
				sb.append(String.format("%02X", b));
			}
			sb.append("\n");
			sb.append(" payload: ");
			for (byte b : records[i].getPayload()) {
				sb.append(String.format("%02X", b));
			}
			sb.append("\n");
			sb.append(" tnf: ");
			switch (records[i].getTnf()) {
			case NdefRecord.TNF_EMPTY:
				sb.append("TNF_EMPTY");
				break;
			case NdefRecord.TNF_WELL_KNOWN:
				sb.append("TNF_WELL_KNOWN");
				break;
			case NdefRecord.TNF_MIME_MEDIA:
				sb.append("TNF_MIME_MEDIA");
				break;
			case NdefRecord.TNF_ABSOLUTE_URI:
				sb.append("TNF_ABSOLUTE_URI");
				break;
			case NdefRecord.TNF_EXTERNAL_TYPE:
				sb.append("TNF_EXTERNAL_TYPE");
				break;
			case NdefRecord.TNF_UNKNOWN:
				sb.append("TNF_UNKNOWN");
				break;
			case NdefRecord.TNF_UNCHANGED:
				sb.append("TNF_UNCHANGED");
				break;
			}
			sb.append("\n");
			sb.append(" type: ");
			for (byte b : records[i].getType()) {
				sb.append(String.format("%02X", b));
			}
			sb.append("\n");
		}
	}
}
