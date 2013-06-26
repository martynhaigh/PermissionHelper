PermissionHelper
================

Overview
--------
A test project to invetigate the how to acess permissions using a 'helper' APK which can be installed at a later stage - effectively providing permissions on a request basis.  The idea is that through AIDL the Consumer can communiate with the Provider and register a callback to be invoced when the requested action has completed, in this case the reading of SMS messages.

Security
--------
By providing the expected Consumer signature key in to the Provider, we can restrict access to the Provider functionality.

Sign either Consumer and Provider with debug2.keystore and the permission sharing will work, however if you sign either Consumer with debug.keystore then the Provider will not return any data.
