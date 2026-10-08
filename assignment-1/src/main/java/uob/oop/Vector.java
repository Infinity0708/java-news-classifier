package uob.oop;

public class Vector {
    private double[] doubElements;

    public Vector(double[] _elements) {
        doubElements = _elements;
    }

    public double getElementatIndex(int _index) {
        if (_index < doubElements.length && _index >= 0) {
            return doubElements[_index];
        }

        return -1;
    }

    public void setElementatIndex(double _value, int _index) {
        if (_index >= 0 && _index < doubElements.length) {
            doubElements[_index] = _value;
        } else {
            doubElements[doubElements.length - 1] = _value;
        }

    }

    public double[] getAllElements() {

        return doubElements;
    }

    public int getVectorSize() {
        return doubElements.length;
    }

    public Vector reSize(int _size) {
        if (_size == doubElements.length || _size <= 0) {
            return new Vector(doubElements);
        } else if (_size < doubElements.length) {
            Vector vector = new Vector(new double[_size]);
            System.arraycopy(this.doubElements, 0, vector.doubElements, 0, _size);
            return vector;
        } else {
            Vector vector = new Vector(new double[_size]);
            for (int i = 0; i < _size; i++) {
                vector.doubElements[i] = -1;
            }
            System.arraycopy(doubElements, 0, vector.doubElements, 0, doubElements.length);
            return vector;

        }

    }

    public Vector add(Vector _v) {
        Vector a = this;
        Vector b = _v;

        if (a.getVectorSize() > b.getVectorSize()) {
            b = b.reSize(a.getVectorSize());
        } else {
            a = a.reSize(b.getVectorSize());
        }
        double[] newDoubElement = new double[a.getVectorSize()];
        for (int i = 0; i < a.getVectorSize(); i++) {
            newDoubElement[i] = a.doubElements[i] + b.doubElements[i];
        }
        return new Vector(newDoubElement);
    }

    public Vector subtraction(Vector _v) {
        Vector a = this;
        Vector b = _v;

        if (a.getVectorSize() > b.getVectorSize()) {
            b = b.reSize(a.getVectorSize());
        } else {
            a = a.reSize(b.getVectorSize());
        }
        double[] newDoubElement = new double[a.getVectorSize()];
        for (int i = 0; i < a.getVectorSize(); i++) {
            newDoubElement[i] = a.doubElements[i] - b.doubElements[i];
        }
        return new Vector(newDoubElement);
    }

    public double dotProduct(Vector _v) {

        Vector a = this;
        Vector b = _v;

        if (a.getVectorSize() > b.getVectorSize()) {
            b = b.reSize(a.getVectorSize());
        } else {
            a = a.reSize(b.getVectorSize());
        }
        double sum = 0;
        for (int i = 0; i < a.getVectorSize(); i++) {
            sum += a.doubElements[i] * b.doubElements[i];
        }
        return sum;
    }

    public double cosineSimilarity(Vector _v) {
        Vector a = this;
        Vector b = _v;

        if (a.getVectorSize() > b.getVectorSize()) {
            b = b.reSize(a.getVectorSize());
        } else {
            a = a.reSize(b.getVectorSize());
        }
        return a.dotProduct(b) / (Math.sqrt(a.dotProduct(a)) * Math.sqrt(b.dotProduct(b)));

    }

    @Override
    public boolean equals(Object _obj) {
        Vector v = (Vector) _obj;
        boolean boolEquals = true;

        if (this.getVectorSize() != v.getVectorSize())
            return false;

        for (int i = 0; i < this.getVectorSize(); i++) {
            if (this.getElementatIndex(i) != v.getElementatIndex(i)) {
                boolEquals = false;
                break;
            }
        }
        return boolEquals;
    }

    @Override
    public String toString() {
        StringBuilder mySB = new StringBuilder();
        for (int i = 0; i < this.getVectorSize(); i++) {
            mySB.append(String.format("%.5f", doubElements[i])).append(",");
        }
        mySB.delete(mySB.length() - 1, mySB.length());
        return mySB.toString();
    }
}
