package mirror;

import java.awt.Dimension;

import javax.media.Buffer;
import javax.media.Controls;
import javax.media.Effect;
import javax.media.Format;
import javax.media.ResourceUnavailableException;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;

public class HahaEffect implements Effect {

	private static final String EffectName = "HahaEffect";
	protected RGBFormat inputFormat;
	protected RGBFormat outputFormat;

	protected Format[] supportedInputFormats;// 所有支持的输入格式
	protected Format[] supportedOutputFormats;// 输出格式
	private int i;

	public HahaEffect() {
		// TODO Auto-generated constructor stub
		supportedInputFormats = new Format[] { new RGBFormat() };

		supportedOutputFormats = new Format[] { new RGBFormat() };
	}

	public HahaEffect(int i) {
		// TODO Auto-generated constructor stub
		this.i = i;
		supportedInputFormats = new Format[] { new RGBFormat() };

		supportedOutputFormats = new Format[] { new RGBFormat() };
	}

	@Override
	public Format[] getSupportedInputFormats() {
		// TODO Auto-generated method stub
		return supportedInputFormats;
	}

	@Override
	public Format[] getSupportedOutputFormats(Format parm1) {
		// TODO Auto-generated method stub
		if (parm1 == null) {
			return supportedOutputFormats;
		}
		if (!(parm1 instanceof RGBFormat)) {
			return new Format[0];
		}
		RGBFormat orf = (RGBFormat) parm1.clone();
		return new Format[] { orf };
	}

	@Override
	public int process(Buffer parm1, Buffer parm2) {
		// TODO Auto-generated method stub
		Dimension size = ((VideoFormat) parm1.getFormat()).getSize();
		int inWidth = size.width;
		int inHeight = size.height;
		Object srcData = parm1.getData();
		if (!(srcData instanceof byte[])) {
			return this.BUFFER_PROCESSED_FAILED;
		}
		Object outdata = null;
		if (i == 0) {
			outdata = this.hahaMirror_GY((byte[]) srcData, inWidth, inHeight);
		}
		if (i == 1) {
			outdata = this.hahaMirror_ZXLC((byte[]) srcData, inWidth, inHeight);
		}
		if (i == 2) {
			outdata = this.hahaMirror_ZXNT((byte[]) srcData, inWidth, inHeight);
		}
		if (i == 3) {
			outdata = this.hahaMirror_FH((byte[]) srcData, inWidth, inHeight);
		}
		if (i == 4) {
			outdata = this.hahaMirror_ZXNA((byte[]) srcData, inWidth, inHeight);
		}

		parm2.setData(outdata);
		int inLength = parm1.getLength();
		int inOffset = parm1.getOffset();

		parm2.setFormat(parm1.getFormat());
		parm2.setLength(inLength);
		parm2.setOffset(inOffset);

		return this.BUFFER_PROCESSED_OK;
	}

	private byte[] hahaMirror_ZXNA(byte[] srcData, int w, int h) {

		// TODO Auto-generated method stub
		// 内凹
		int cenXt = w / 2;
		int cenYt = h / 4;

		int cenXb = w / 2;
		int cenYb = h / 4 * 3;

		int newXt = 0;
		int newYt = 0;

		int newXb = 0;
		int newYb = 0;

		int radiusb = 0;

		int therash = w / 5;

		int m = 2;
		int rate = therash / m;

		int radiust = 0;
		double theta = 0;
		byte[] tempData = (byte[]) srcData.clone();
		int length = srcData.length;
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				int tX = i - cenXt;
				int tY = j - cenYt;
				theta = Math.atan2((double) tY, (double) tX);
				radiust = (int) Math.sqrt((double) (tX * tX + tY * tY));
				int newR = (int) (Math.sqrt((double) radiust) * 12);
				newXt = cenXt + (int) (newR * Math.cos(theta));
				newYt = cenYt + (int) (newR * Math.sin(theta));

				if (newXt > 0 && newXt < w) {

				} else {
					newXt = 0;
				}
				if (newYt > 0 && newYt < h) {

				} else {
					newYt = 0;
				}
				int tempLocation1 = i * 3 + j * w * 3;
				int tempLocation2 = i * 3 + 1 + j * w * 3;
				int tempLocation3 = i * 3 + 2 + j * w * 3;
				int srcLocation1 = newXt * 3 + newYt * w * 3;
				int srcLocation2 = newXt * 3 + 1 + newYt * w * 3;
				int srcLocation3 = newXt * 3 + 2 + newYt * w * 3;
				if ((tempLocation1 <= length) && (tempLocation2 <= length) && (tempLocation3 <= length)
						&& (srcLocation1 <= length) && (srcLocation2 <= length) && (srcLocation3 <= length)) {
					tempData[tempLocation1] = srcData[srcLocation1];
					tempData[tempLocation2] = srcData[srcLocation2];
					tempData[tempLocation3] = srcData[srcLocation3];
				}
			}
		}
		return tempData;
	}

	private byte[] hahaMirror_ZXNT(byte[] srcData, int w, int h) {
		// TODO Auto-generated method stub
		// 中心圆点外凸
		int cenX = w / 2;
		int cenY = h / 2;
		int newX = 0, newY = 0;
		int therash = w / 3;
		byte[] tempData = (byte[]) srcData.clone();
		int length = srcData.length;
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				int m = 3;
				int rate = therash / m;

				if (Math.sqrt((cenX - i) * (cenX - i) + (cenY - j) * (cenY - j)) < therash) {
					newX = (i - cenX) / m;
					newY = (j - cenY) / m;
					newX = (int) (newX * Math.sqrt((cenX - i) * (cenX - i) + (cenY - j) * (cenY - j)) / rate) + cenX;
					newY = (int) (newY * Math.sqrt((cenX - i) * (cenX - i) + (cenY - j) * (cenY - j)) / rate) + cenY;
					int tempLocation1 = i * 3 + j * w * 3;
					int tempLocation2 = i * 3 + 1 + j * w * 3;
					int tempLocation3 = i * 3 + 2 + j * w * 3;
					int srcLocation1 = newX * 3 + newY * w * 3;
					int srcLocation2 = newX * 3 + 1 + newY * w * 3;
					int srcLocation3 = newX * 3 + 2 + newY * w * 3;
					if ((tempLocation1 <= length) && (tempLocation2 <= length) && (tempLocation3 <= length)
							&& (srcLocation1 <= length) && (srcLocation2 <= length) && (srcLocation3 <= length)) {
						tempData[tempLocation1] = srcData[srcLocation1];
						tempData[tempLocation2] = srcData[srcLocation2];
						tempData[tempLocation3] = srcData[srcLocation3];
					}
				}

			}
		}

		return tempData;
	}

	private byte[] hahaMirror_GY(byte[] srcData, int w, int h) {
		// TODO Auto-generated method stub
		//鬼影特效
		int oldX = 0;
		int oldY = 0;
		byte[] tempData = (byte[]) srcData.clone();
		int length = srcData.length;
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				oldX = i / 2;
				oldY = j / 2;
				int tempLocation1 = i * 3 + j * w * 3;
				int tempLocation2 = i * 3 + 1 + j * w * 3;
				int tempLocation3 = i * 3 + 2 + j * w * 3;
				int srcLocation1 = oldX * 3 + oldY * w * 3;
				int srcLocation2 = oldX * 5 + 1 + oldY * w * 6;
				int srcLocation3 = oldX * 2 + 3 + oldY * w * 6;
				if ((tempLocation1 <= length) && (tempLocation2 <= length) && (tempLocation3 <= length)
						&& (srcLocation1 <= length) && (srcLocation2 <= length) && (srcLocation3 <= length)) {
					tempData[tempLocation1] = srcData[srcLocation1];
					tempData[tempLocation2] = srcData[srcLocation2];
					tempData[tempLocation3] = srcData[srcLocation3];
				}

			}
		}
		return tempData;
	}

	private byte[] hahaMirror_ZXLC(byte[] srcData, int w, int h) {
		// TODO Auto-generated method stub
		//纵向拉长
		int oldX = 0;
		int oldY = 0;
		byte[] tempData = (byte[]) srcData.clone();
		int length = srcData.length;
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				oldX = i;
				oldY = (int) ((float) j / 1.5);
				int tempLocation1 = i * 3 + j * w * 3;
				int tempLocation2 = i * 3 + 1 + j * w * 3;
				int tempLocation3 = i * 3 + 2 + j * w * 3;
				int srcLocation1 = oldX * 3 + oldY * w * 3;
				int srcLocation2 = oldX * 3 + 1 + oldY * w * 3;
				int srcLocation3 = oldX * 3 + 2 + oldY * w * 3;

				if ((tempLocation1 <= length) && (tempLocation2 <= length) && (tempLocation3 <= length)
						&& (srcLocation1 <= length) && (srcLocation2 <= length) && (srcLocation3 <= length)) {
					tempData[tempLocation1] = srcData[srcLocation1];
					tempData[tempLocation2] = srcData[srcLocation2];
					tempData[tempLocation3] = srcData[srcLocation3];
				}

			}
		}
		return tempData;
	}

	private byte[] hahaMirror_FH(byte[] srcData, int w, int h) {
		//复合哈哈镜
		int cenXt = w / 2;
		int cenYt = h / 4;

		int cenXb = w / 2;
		int cenYb = h / 4 * 3;

		int newXt = 0;
		int newYt = 0;

		int newXb = 0;
		int newYb = 0;

		int radiusb = 0;

		int therash = w / 5;

		int m = 2;
		int rate = therash / m;

		int radiust = 0;
		double theta = 0;
		byte[] tempData = (byte[]) srcData.clone();
		int length = srcData.length;
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {

				if (j < h / 2) {
					int tX = i - cenXt;
					int tY = j - cenYt;
					theta = Math.atan2((double) tY, (double) tX);
					radiust = (int) Math.sqrt((double) (tX * tX + tY * tY));
					int newR = (int) (Math.sqrt((double) radiust) * 12);
					newXt = cenXt + (int) (newR * Math.cos(theta));
					newYt = cenYt + (int) (newR * Math.sin(theta));

					if (newXt > 0 && newXt < w) {

					} else {
						newXt = 0;
					}
					if (newYt > 0 && newYt < h) {

					} else {
						newYt = 0;
					}
					int tempLocation1 = i * 3 + j * w * 3;
					int tempLocation2 = i * 3 + 1 + j * w * 3;
					int tempLocation3 = i * 3 + 2 + j * w * 3;
					int srcLocation1 = newXt * 3 + newYt * w * 3;
					int srcLocation2 = newXt * 3 + 1 + newYt * w * 3;
					int srcLocation3 = newXt * 3 + 2 + newYt * w * 3;
					if ((tempLocation1 <= length) && (tempLocation2 <= length) && (tempLocation3 <= length)
							&& (srcLocation1 <= length) && (srcLocation2 <= length) && (srcLocation3 <= length)) {
						tempData[tempLocation1] = srcData[srcLocation1];
						tempData[tempLocation2] = srcData[srcLocation2];
						tempData[tempLocation3] = srcData[srcLocation3];
					}
				} else {

					int tX = i - cenXb;
					int tY = j - cenYb;
					radiusb = (int) Math.sqrt((double) (tX * tX + tY * tY));

					if (radiusb < therash) {
						newXb = tX / m;
						newYb = tY / m;
						newXb = (int) (newXb * radiusb / therash) + cenXb;
						newYb = (int) (newYb * radiusb / therash) + cenYb;
						int tempLocation1 = i * 3 + j * w * 3;
						int tempLocation2 = i * 3 + 1 + j * w * 3;
						int tempLocation3 = i * 3 + 2 + j * w * 3;
						int srcLocation1 = newXb * 3 + newYb * w * 3;
						int srcLocation2 = newXb * 3 + 1 + newYb * w * 3;
						int srcLocation3 = newXb * 3 + 2 + newYb * w * 3;
						if ((tempLocation1 < length) && (tempLocation2 < length) && (tempLocation3 < length)
								&& (srcLocation1 < length) && (srcLocation2 < length) && (srcLocation3 < length)) {
							tempData[tempLocation1] = srcData[srcLocation1];
							tempData[tempLocation2] = srcData[srcLocation2];
							tempData[tempLocation3] = srcData[srcLocation3];
						}
					} else {

					}
				}

			}
		}

		return tempData;
	}

	@Override
	public Format setInputFormat(Format parm1) {
		// TODO Auto-generated method stub
		inputFormat = (RGBFormat) parm1;
		return (Format) inputFormat;
	}

	@Override
	public Format setOutputFormat(Format parm1) {
		// TODO Auto-generated method stub
		outputFormat = (RGBFormat) parm1;
		return (Format) outputFormat;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return EffectName;
	}

	@Override
	public void open() throws ResourceUnavailableException {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getControl(String arg0) {
		// TODO Auto-generated method stub
		try {
			Class cls = Class.forName(arg0);
			Object[] cs = this.getControls();
			for (int i = 0; i < cs.length; i++) {
				if (cls.isInstance(cs[i])) {
					return cs[i];
				}
			}
			return null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public Object[] getControls() {
		// TODO Auto-generated method stub
		return new Controls[0];
	}

}
