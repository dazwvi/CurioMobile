-----------------------
Development environment
-----------------------
Android: Android Studio 2.1.2
Android 6.0 "Marshmallow" (API 23)
OS Name: Mac OS X
OS Version: 10.11.4
OS Architecture: x86_64
Total Memory (MB): 61
Max Memory (MB): 910

--------------
Overall Design
--------------
ItemDetailActivity: AppCompatActivity for handling item detail view.
ItemDetailFragment: Fragment that included in the ItemDetailAvtivity for showing item details.
ItemListActivity: AppCompatActivity for handling master list view.
MasterList: class to store the master list fields.
GetRequestTask: AsyncTask for send GET http request to get JSON objects.
ImageLoadTask: AsyncTask for send GET http request to get images.

------------
Enhancements
------------
1. Advanced search: ability to search by a specific field from the possible fields supported by the project search GET request. For fields: description, data type, name, short description and redirect url.

-----
Note
-----
For the Advanced search, you need for select “Search by XXX” from the drop down list.