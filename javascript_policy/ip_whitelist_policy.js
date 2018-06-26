var context = $evaluation.getContext();
var contextAttributes = context.getAttributes();

function readFully(url) {
    var result = "";
    var imports = new JavaImporter(java.net, java.lang, java.io);

    with (imports) {
        var urlObj = null;
        try {
            urlObj = new URL(url);
        } catch (e) {
            // If the URL cannot be built, assume it is a file path.
            urlObj = new URL(new File(url).toURI().toURL());
        }
        var reader = new BufferedReader(new InputStreamReader(urlObj.openStream()));
        var line = reader.readLine();
        while (line != null) {
            result += line + "\n";
            line = reader.readLine();
        }
        reader.close();
    }
    return result;
}


var ip_addresses = readFully('ip_addresses.txt');
print(ip_addresses)

var i;
for (i = 0; i < ip_addresses.length; i++) {
    if (contextAttributes.containsValue('kc.client.network.ip_address', ip_addresses[i])) {
        print('OK')
        $evaluation.grant();
    }
}
