package sbilife.com.pointofsale_bancaagency;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.common.StorageUtils;

public class ProposerCaptureSignature extends AppCompatActivity {

	private LinearLayout mContent;
	private signature mSignature;
	private Button mClear;
	private Button mGetSign;
	private Button mCancel;
	public int count = 1;
	private String current = null;
	private Bitmap mBitmap;
	private View mView;
	private File mypath;

	public static Bitmap scaled;
	private StorageUtils mStorageUtils;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_signature);

		mStorageUtils = new StorageUtils();

		Intent intent = getIntent();

        String uniqueId = intent.getStringExtra("uniqueId");
		current = uniqueId + ".png";
//		File mypath = new File(folder, FileName + ".pdf");
		
		mypath = mStorageUtils.createFileToAppSpecificDir(this, current);

		mContent = findViewById(R.id.linearLayout);
		mSignature = new signature(this, null);
		mSignature.setBackgroundColor(Color.WHITE);
		mContent.addView(mSignature, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mClear = findViewById(R.id.clear);
		mGetSign = findViewById(R.id.getsign);
		mGetSign.setEnabled(false);
		mCancel = findViewById(R.id.cancel);
		mView = mContent;

		mClear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.v("log_tag", "Panel Cleared");
				mSignature.clear();
				mGetSign.setEnabled(false);
			}
		});

		mGetSign.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Log.v("log_tag", "Panel Saved");
				boolean error = captureSignature();
				if (!error) {
					Intent in = getIntent();
					String test = in.getStringExtra("Test");
					mView.setDrawingCacheEnabled(true);
					mSignature.save(mView);
					Bundle b = new Bundle();
					b.putString("status", "done");

					Intent intent = new Intent();
					intent.putExtras(b);
					intent.putExtra("Test", test);
					setResult(RESULT_OK, intent);
					finish();

				}

				scaled = Bitmap.createScaledBitmap(mBitmap, 300, 80, true);

			}

		});

		mCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.v("log_tag", "Panel Canceled");
				Bundle b = new Bundle();
				b.putString("status", "cancel");
				Intent intent = new Intent();
				intent.putExtras(b);
				setResult(RESULT_OK, intent);
				finish();
			}
		});

	}

	@Override
	protected void onDestroy() {
		Log.w("GetSignature", "onDestory");

		super.onDestroy();

	}

	private boolean captureSignature() {

		boolean error = false;
		String errorMessage = "";

		if (error) {
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP, 105, 50);
			toast.show();
		}

		return error;
	}

	/*private String getTodaysDate() {

		final Calendar c = Calendar.getInstance();
		int todaysDate = (c.get(Calendar.YEAR) * 10000)
				+ ((c.get(Calendar.MONTH) + 1) * 100)
				+ (c.get(Calendar.DAY_OF_MONTH));
		Log.w("DATE:", String.valueOf(todaysDate));
		return (String.valueOf(todaysDate));

	}*/

	/*private String getCurrentTime() {

		final Calendar c = Calendar.getInstance();
		int currentTime = (c.get(Calendar.HOUR_OF_DAY) * 10000)
				+ (c.get(Calendar.MINUTE) * 100) + (c.get(Calendar.SECOND));
		Log.w("TIME:", String.valueOf(currentTime));
		return (String.valueOf(currentTime));

	}*/

//	private boolean prepareDirectory() {
//		try {
//			if (makedirs()) {
//				return true;
//			} else {
//				return false;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			Toast.makeText(
//					this,
//					"Could not initiate File System.. Is Sdcard mounted properly?",
//					1000).show();
//			return false;
//		}
//	}

//	private boolean makedirs() {
//		File tempdir = new File(tempDir);
//		if (!tempdir.exists())
//			tempdir.mkdirs();
//
//		if (tempdir.isDirectory()) {
//			File[] files = tempdir.listFiles();
//			for (File file : files) {
//				if (!file.delete()) {
//					System.out.println("Failed to delete " + file);
//				}
//			}
//		}
//		return (tempdir.isDirectory());
//	}

	public class signature extends View {
		private static final float STROKE_WIDTH = 5f;
		private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
		private Paint paint = new Paint();
		private Path path = new Path();

		private float lastTouchX;
		private float lastTouchY;
		private final RectF dirtyRect = new RectF();

		public signature(Context context, AttributeSet attrs) {
			super(context, attrs);
			paint.setAntiAlias(true);
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeWidth(STROKE_WIDTH);
		}

		void save(View v) {
			Log.v("log_tag", "Width: " + v.getWidth() + "  "+mContent.getWidth());
			Log.v("log_tag", "Height: " + v.getHeight()+ "  "+mContent.getHeight());
			if (mBitmap == null) {
				mBitmap = Bitmap.createBitmap(v.getWidth(),
						v.getHeight(), Bitmap.Config.RGB_565);//RGB_565
			}
			Canvas canvas = new Canvas(mBitmap);
			try
			{
				FileOutputStream mFileOutStream = new FileOutputStream(mypath);

				v.draw(canvas);
				mBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
				mFileOutStream.flush();
				mFileOutStream.close();
				//				String url = Images.Media.insertImage(getContentResolver(),
				//						mBitmap, "title", null);
				//				Log.v("log_tag", "url: " + url);

			} catch (Exception e) {
				Log.v("log_tag", e.toString());
			}
		}

		void clear() {
			path.reset();
			invalidate();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawPath(path, paint);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float eventX = event.getX();
			float eventY = event.getY();
			mGetSign.setEnabled(true);

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				path.moveTo(eventX, eventY);
				lastTouchX = eventX;
				lastTouchY = eventY;
				return true;

			case MotionEvent.ACTION_MOVE:

			case MotionEvent.ACTION_UP:

				resetDirtyRect(eventX, eventY);
				int historySize = event.getHistorySize();
				for (int i = 0; i < historySize; i++) {
					float historicalX = event.getHistoricalX(i);
					float historicalY = event.getHistoricalY(i);
					expandDirtyRect(historicalX, historicalY);
					path.lineTo(historicalX, historicalY);
				}
				path.lineTo(eventX, eventY);
				break;

			default:
				debug("Ignored touch event: " + event.toString());
				return false;
			}

			invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
					(int) (dirtyRect.top - HALF_STROKE_WIDTH),
					(int) (dirtyRect.right + HALF_STROKE_WIDTH),
					(int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

			lastTouchX = eventX;
			lastTouchY = eventY;

			return true;
		}

		private void debug(String string) {
		}

		private void expandDirtyRect(float historicalX, float historicalY) {
			if (historicalX < dirtyRect.left) {
				dirtyRect.left = historicalX;
			} else if (historicalX > dirtyRect.right) {
				dirtyRect.right = historicalX;
			}

			if (historicalY < dirtyRect.top) {
				dirtyRect.top = historicalY;
			} else if (historicalY > dirtyRect.bottom) {
				dirtyRect.bottom = historicalY;
			}
		}

		private void resetDirtyRect(float eventX, float eventY) {
			dirtyRect.left = Math.min(lastTouchX, eventX);
			dirtyRect.right = Math.max(lastTouchX, eventX);
			dirtyRect.top = Math.min(lastTouchY, eventY);
			dirtyRect.bottom = Math.max(lastTouchY, eventY);
		}
	}
}