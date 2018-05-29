#pragma version(1)
#pragma rs java_package_name(com.example.bav.renderscriptexample)

int *aArray;
int aSize;

int RS_KERNEL execute(int in) {
    // Binary search
    int first = 0;
    int last = aSize - 1;
    int middle = (first + last) / 2;

    while (first <= last) {
        if (aArray[middle] < in) {
            first = middle + 1;
        }
        else if (aArray[middle] == in) {
            return in;
        }
        else {
            last = middle - 1;
        }
        middle = (first + last) / 2;
    }

    return 0;
}
