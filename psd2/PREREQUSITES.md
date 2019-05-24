1. Create accounts realm
2. Create accounts realm role, set it as the only default role
3. Create client template called psd2-template
4. Set accounts realm role in psd2-templates scope
5. Setup Bacic Auth authentication for the Browser binding

6. Create payments realm
7. Create payments realm role, set it as the only default role
8. Create client template called psd2-template
9. Set payments realm role in psd2-templates scope
10. Setup Bacic Auth authentication for the Browser binding

6. Create fundsconfirmations realm
7. Create fundsconfirmations realm role, set it as the only default role
8. Create client template called psd2-template
9. Set payments realm rolw in psd2-templates scope
10. Setup Bacic Auth authentication for the Browser binding

11. Create psd2-registration in Master Realm.
    * Must Have service Account access
        * Service Account Roles:
            * In accounts-realm : manage-clients, manage-users
            * In payments-realm : manage-clients, manage-users
            * In fundsconfirmations-realm : manage-clients, manage-users
            
            
These script requires jq, a command line to to parse and format JSon. https://stedolan.github.io/jq/            