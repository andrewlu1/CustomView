package cn.andrewlu.app.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;


public class BlurImageView extends View {
    private Bitmap mBlurBitmap;
    private Bitmap mBgBitmap;

    public BlurImageView(Context context) {
        super(context);
    }

    public BlurImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, android.R.styleable.ImageView);
        Drawable drawable = array.getDrawable(android.R.styleable.ImageView_src);
        if (drawable != null) {
            setBackground(drawable);
        }
        array.recycle();
    }

    public BlurImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    public final void setBackground(Drawable background) {
        if (background != null && background instanceof BitmapDrawable) {
            setBackground(((BitmapDrawable) background).getBitmap());
        } else {
            super.setBackground(background);
        }
    }

    public void setBackground(int background) {
        Drawable drawable = getResources().getDrawable(background);
        setBackground(drawable);
    }

    public void setBackground(Bitmap background) {
        mBgBitmap = background;
        if (background != null) {
            Bitmap bmp = background;
            mBlurBitmap = BoxBlur.blur(bmp, 10, 20);
        } else {
            mBlurBitmap = null;
        }
        postInvalidate();
    }

    public void setBlurFactor(float factor) {
        mBlurFactor = factor;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Rect dst = new Rect(0, 0, getWidth(), getHeight());
        mPaint.setAlpha((int) (mBlurFactor * 255));
        if (mBgBitmap != null) {
            Rect src = new Rect(0, 0, mBgBitmap.getWidth(), mBgBitmap.getHeight());
            clipRect(dst, src);
            canvas.drawBitmap(mBgBitmap, src, dst, mPaint);
        }
        if (mBlurBitmap != null) {
            Rect src = new Rect(0, 0, mBlurBitmap.getWidth(), mBlurBitmap.getHeight());
            clipRect(dst, src);
            canvas.drawBitmap(mBlurBitmap, src, dst, mPaint);
        }
    }

    private Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
    private float mBlurFactor = 0.5f;

    //this will change src rect.
    //保持src 宽或高不动，另一边以dst的宽高比剪裁一部分，然后矩形对齐到中间部位。
    private void clipRect(Rect dst, Rect src) {
        if (dst == null || src == null) return;
        if (src.width() == 0 || src.height() == 0 || dst.width() == 0 || dst.height() == 0) return;
        float whDst = ((float) dst.width()) / dst.height();
        float whSrc = ((float) src.width()) / src.height();
        if (whDst <= whSrc) {
            int w = (int) (src.height() * whDst);
            int paddingLeft = (src.width() - w) / 2;
            src.set(src.left + paddingLeft, src.top, src.right - paddingLeft, src.bottom);
        } else {
            int h = (int) (src.width() * whDst);
            int paddingTop = (src.height() - h) / 2;
            src.set(src.left, src.top + paddingTop, src.right, src.bottom - paddingTop);
        }
    }

    private final static class BoxBlur {
        public static Bitmap blur(Bitmap inputBmp, int radius, int iterations) {

            Bitmap bitmap = getOutputBitmap(inputBmp, null);

            if (iterations < 1) iterations = 1;
            if (iterations > 3) iterations = 3;
            if (radius < 1) radius = 1;

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            int[] pix = new int[w * h];
            bitmap.getPixels(pix, 0, w, 0, 0, w, h);

            while (iterations-- > 0) {
                boxBlurVertical(pix, w, h, radius);
                boxBlurHorizontal(pix, w, h, radius);
            }
            bitmap.setPixels(pix, 0, w, 0, 0, w, h);
            return bitmap;
        }

        private static void boxBlurHorizontal(int[] pixels, int w, int h,
                                              int halfRange) {
            int index = 0;
            int[] newColors = new int[w];

            for (int y = 0; y < h; y++) {
                int hits = 0;
                long r = 0;
                long g = 0;
                long b = 0;
                for (int x = -halfRange; x < w; x++) {
                    int oldPixel = x - halfRange - 1;
                    if (oldPixel >= 0) {
                        int color = pixels[index + oldPixel];
                        if (color != 0) {
                            r -= Color.red(color);
                            g -= Color.green(color);
                            b -= Color.blue(color);
                        }
                        hits--;
                    }

                    int newPixel = x + halfRange;
                    if (newPixel < w) {
                        int color = pixels[index + newPixel];
                        if (color != 0) {
                            r += Color.red(color);
                            g += Color.green(color);
                            b += Color.blue(color);
                        }
                        hits++;
                    }

                    if (x >= 0) {
                        newColors[x] = Color.argb(0xFF, (int) (r / hits), (int) (g / hits), (int) (b / hits));
                    }
                }

                for (int x = 0; x < w; x++) {
                    pixels[index + x] = newColors[x];
                }

                index += w;
            }
        }

        private static void boxBlurVertical(int[] pixels, int w, int h,
                                            int halfRange) {

            int[] newColors = new int[h];
            int oldPixelOffset = -(halfRange + 1) * w;
            int newPixelOffset = (halfRange) * w;

            for (int x = 0; x < w; x++) {
                int hits = 0;
                long r = 0;
                long g = 0;
                long b = 0;
                int index = -halfRange * w + x;
                for (int y = -halfRange; y < h; y++) {
                    int oldPixel = y - halfRange - 1;
                    if (oldPixel >= 0) {
                        int color = pixels[index + oldPixelOffset];
                        if (color != 0) {
                            r -= Color.red(color);
                            g -= Color.green(color);
                            b -= Color.blue(color);
                        }
                        hits--;
                    }

                    int newPixel = y + halfRange;
                    if (newPixel < h) {
                        int color = pixels[index + newPixelOffset];
                        if (color != 0) {
                            r += Color.red(color);
                            g += Color.green(color);
                            b += Color.blue(color);
                        }
                        hits++;
                    }

                    if (y >= 0) {
                        newColors[y] = Color.argb(0xFF, (int) (r / hits), (int) (g / hits), (int) (b / hits));
                    }

                    index += w;
                }

                for (int y = 0; y < h; y++) {
                    pixels[y * w + x] = newColors[y];
                }
            }
        }

        private static Bitmap getOutputBitmap(Bitmap inputBitmap, Bitmap outputBitmap) {
            Rect sSrcRect = new Rect(0, 0, inputBitmap.getWidth(), inputBitmap.getHeight());
            Rect sDstRect = new Rect(0, 0, (int) (inputBitmap.getWidth() / 12.0f), (int) (inputBitmap.getHeight() / 12.0f));
            if (sDstRect.width() == 0 || sDstRect.height() == 0) {
                sDstRect.set(0, 0, inputBitmap.getWidth(), inputBitmap.getHeight());
            }
            if (outputBitmap == null
                    || outputBitmap.isRecycled()
                    || outputBitmap.getWidth() != sDstRect.width()
                    || outputBitmap.getHeight() != sDstRect.height()) {
                outputBitmap = Bitmap.createBitmap(sDstRect.width(), sDstRect.height(), Bitmap.Config.ARGB_8888);
            }
            if (outputBitmap != null) {
                Canvas canvas = new Canvas(outputBitmap);
                canvas.setBitmap(outputBitmap);
                canvas.drawBitmap(inputBitmap, sSrcRect, sDstRect, new Paint());
            } else {
                outputBitmap = inputBitmap;
            }
            return outputBitmap;
        }
    }

}
