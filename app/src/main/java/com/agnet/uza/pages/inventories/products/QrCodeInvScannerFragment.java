package com.agnet.uza.pages.inventories.products;


public class QrCodeInvScannerFragment {/* extends BarcodeFragment{implements BarcodeRetriever {

    private FragmentActivity _c;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private BarcodeCapture barcodeCapture;
    private Gson _gson;


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_barcode, container, false);
        _c = getActivity();

        //initialize
        _preferences = getActivity().getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();
        _gson = new Gson();

        //binding
        barcodeCapture = (BarcodeCapture) getChildFragmentManager().findFragmentById(R.id.barcode);

        //event
        barcodeCapture.setRetrieval(this);



        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onRetrieved(final Barcode barcode) {
        _c.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _editor.putString("BARCODE_NUM", barcode.displayValue);
                _editor.commit();

                if(_preferences.getInt("PRODUCT_ACTION_FLAG", 0) == 1){



                    new FragmentHelper(_c).replace(new NewProductFragment(), "NewProductFragment", R.id.fragment_placeholder);

                }else {


                    new FragmentHelper(_c).replace(new EditProductFragment(), "EditProductFragment", R.id.fragment_placeholder);

                }

                _editor.remove("PRODUCT_ACTION_FLAG");
                _editor.commit();

            }
        });

        barcodeCapture.stopScanning();
//
    }


    @Override
    public void onRetrievedMultiple(final Barcode closetToClick, final List<BarcodeGraphic> barcodeGraphics) {
        _c.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message = "Code selected : " + closetToClick.displayValue + "\n\nother " +
                        "codes in frame include : \n";
                for (int index = 0; index < barcodeGraphics.size(); index++) {
                    Barcode barcode = barcodeGraphics.get(index).getBarcode();
                    message += (index + 1) + ". " + barcode.displayValue + "\n";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(_c)
                        .setTitle("code retrieved")
                        .setMessage(message);
                builder.show();
            }
        });
    }


    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }



    @Override
    public void onRetrievedFailed(String reason) {

    }

    @Override
    public void onPermissionRequestDenied() {

    }

    @Override
    public Camera retrieveCamera() {
        return null;
    }



*/

}
