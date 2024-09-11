package me.wisdom.thepit.misc;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class MinecraftSkin {
    public static List<MinecraftSkin> minecraftSkins = new ArrayList<>();

    static {
//		Overworld
        new MinecraftSkin("KyroKrypt",
                "ewogICJ0aW1lc3RhbXAiIDogMTY3MzIyNzUzMDAyNSwKICAicHJvZmlsZUlkIiA6ICIwMWFjYmI0OTYzNTc0NTAyODFjYWU3OWY0YjMxYTQ0ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJLeXJvS3J5cHQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjZhNGFkOGM3ZDRiM2IzMGRlODdiZjc5NTc2MjdiNzdlMWJiYjYzYzFhZjMyNzcyYTg0MWZmYTUxMjVhNzZiIgogICAgfQogIH0KfQ==",
                "rLwh109BDhqN+SKMT2vcfi8ApkxxxWAWlEHroPEDEhSDBB20lTYON256uUtVfoNLB8Ajxls+kj1sUA6Akn/mfsKcVzMT2VeA00DTmgh60tOuR5LtQn8iHxunzmREsLJYXNqW3P6SRA+FD0TWUWeouDhyzknRmn/Y4FEh83v9fed00Rj6JW3tg0Danp814FyZ4KteLEGmXrROBaCut/HUgr77SFuO73MJyesuuX92ApfSZcgNXpMzLo5YDo1JxfFwzMCKJtjsmYwvUvabDLQu8v+KrUIbjxArt+uyzunbTTDNK8mhemtmSM0ds4l8Qp2EcBkhiq9yk7oJsnvr1eqZvvIjCVy2Yn0jGxnRO6Su6bgrluhP/mzumCuGjzWsWW2zkfSkTc7JXHe+ZrvtYZdMfV+BOuxpan8AcG9C4eA+nZ+srvkLi5IPRKb4NoNEYVWVREndSVshV90si6I7t5S6EOoTXO3QBEdFoESYjCGEEveeAHSK5r5Xjc9ylhcP8T8i1OL8IPdSXvqKpsQWEPbW4JSPHhtYPF+IBj5JmvKb3qK819UsjSMHAqwD2105cqV0HSVYXZ+29ebl5Hc2gp69WFOnD/JWr05XDchocr+DTtgD9cuet2PsTqYhjaZJIqS3gOUu3XsfZDVbXrnVskIw67DyFBW2bEqM4kJEIHA7HmY="
        );
        new MinecraftSkin("wiji1",
                "ewogICJ0aW1lc3RhbXAiIDogMTY3NTQ3OTY5OTUxNCwKICAicHJvZmlsZUlkIiA6ICI1YzA2NjI2YzY2YmY0OGE5ODRjYmYyNjgzZTZhY2E4NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJ3aWppMSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mOWNmNzA5NTM0ZjlmMGU0MGZjYjg0YTY1MmRkOTA0M2JlYThhYzEwYzJmNTNmY2EzZWM3MzA2ODU4ZTk4ODkwIgogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yMzQwYzBlMDNkZDI0YTExYjE1YThiMzNjMmE3ZTllMzJhYmIyMDUxYjI0ODFkMGJhN2RlZmQ2MzVjYTdhOTMzIgogICAgfQogIH0KfQ==",
                "xWuzr0re00/xUmR/dLOc3bjgZzqkJ5IVzVDVuE1s6/D5sdyFypfKHA0palivvnhiiIrmZV8pxdHX5MBOuoegw+PDmce0n4kt/aEKfSM2CIEfCDrXOV4volKhKowo2ulCkO8s6BG7ZOaF4RAKWHOivrvcYtNUOyodjwvJ+Ie6fexVxPN7REqdP0Wa4JSJ98VXmFhO4wdQSAxt7cM5zT+CriPWXWymJujmEMN0dK6+2PhDJv1PxbYPIEwgp2yU0TwIWKsp8Uci7V30K7KMHVaFMR78ED/6x4hSPqNpTpN7Z0yGot3fS+G4B3Ef0ir1BwZlzBzZodGP+6GwfZxoDdmT0vvCHe8j+oci4tKSHLADMOGtoFycKrj12TMvcuLMKBQw012npdqO9vC0TPS4dFr1G2CiW3PSyIz7q90bqvjA+ks0/nhYfmOpGNkBk045LRLUj5bQFbDEh6V3pnXhPq9+2jf9Oxk5tp+aqw2tPZuoV9daI09x4pari92mACsoYCu6Ux+/jlANgYVeqCZZHPR6CJ3YEeD0uHqJwaz2gTzFsz8BLSebDq9YsZ8SyQRCJ+2kbiW2XaT4NZI541GbTgX3WAee4OttBoMfCYD1ZYGmVwuyCgJfhYdyV9k0in4RIbjchWzTFkVTkFfHPvtZPPBrDpvs70G3MNKdPE2dYOy0DY4="
        );
        new MinecraftSkin("Sammymon",
                "ewogICJ0aW1lc3RhbXAiIDogMTY4MDM4NzI3OTA4NiwKICAicHJvZmlsZUlkIiA6ICIyZDhmZjJlNGFhMjY0NDA0YTI2M2U3OTJhZThlMWQxZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJTYW1teW1vbiIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81M2I2ZWMxY2Q5NjRlZTIxZDU1ODgyNWQ1NjNkNzMyYjQzMzgxMmYzOWEyNTY1ZTQzMWRmMWZmNzI3YzNlZmNhIgogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yMzQwYzBlMDNkZDI0YTExYjE1YThiMzNjMmE3ZTllMzJhYmIyMDUxYjI0ODFkMGJhN2RlZmQ2MzVjYTdhOTMzIgogICAgfQogIH0KfQ==",
                "UmPBlJj49YwjBxwB6Kmlcrqi0Yv1Q/fHv2sNhA6n5oKVsYAkA7SixJT8oZPwSTL9Ztqv0x/Gy3Bt8AXoel1XifLjGnttu3krQjug6ZKasoL533bdBUjlLjcUKugrc1rAo0qsU4AAqHF8v+BDM440VLxO881g6d/JFc05Eij+LwvUqrCYlxk6Uwru4QhSCRjDk6K9hUqryqsHMKr2f/bBIiZyXy37lBv0URFwoNcat26EpkuxZB4tC4k33b6Daye9CwmkU9s1WbBqWOT6XvUN9C1VR3Bmq7JGQRYCJp5aXTw0daoaKtk7YsXGihr5IMb/aPfGeb4MmINtognyRq9j81XpmN+xBOOHvuPcWJCIdmXrEYMmYX2gNBfwwKFNon6Dx7OMa5J1l/n3V0AyJCLFKKr0L49u908OPHQV25eXw22JdCtz5YkuzHasiH1VEJvBPbyEBZgqJLrN+1eU4OJRXL7cbNsoWye7k8DHdNomwNUKbc7ReMkVH1gzxvvp/LM8Bgb300Uym/hoyEuhs+gLoarWKniBdNlPUIQKu10aRoe8kaFZW7AgUfJZdDN028CPpyglhi40y5fBCVS4MPHYWJxMzfuC71MjJA4Lmla6/99hrdZc+1tZjbhxBUWQDVCM1UJmwG1Jbapzpk/hyPjXquw2AqWiLNlRy7bvw6KYOLc="
        );
        new MinecraftSkin("Revernal",
                "ewogICJ0aW1lc3RhbXAiIDogMTY3MzIyNzgxNzExMSwKICAicHJvZmlsZUlkIiA6ICJjMjZmMTg4MmU1ZGM0NzMxYjgxMWFhZjdmZDM0MWFmNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJSZXZlcm5hbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85OWRjMjdlYWQxZDg4NDY5MmY4ZDlkYWNkNzAyYjE2OWM0ODBmMmVhZjgwMzUwOTY5MTgxNzZlY2JlOTdlZDk5IgogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yMzQwYzBlMDNkZDI0YTExYjE1YThiMzNjMmE3ZTllMzJhYmIyMDUxYjI0ODFkMGJhN2RlZmQ2MzVjYTdhOTMzIgogICAgfQogIH0KfQ==",
                "oIJT7L2Kef5vybT77JyfAvpavvaln0I1yKxVaLotJ13pRsoj2sQPUOomUnxGW4xhS72mfu3Kab/5erp2T4VT9L1311fHG4PuGYLxKZZ9751oM2Hil3st75a+ymdYZ5HdTeJWrnJh48wzNwNvEEAVl7WYjH6vzaPSyUtD/k0fTaALl1qsAgUjINcrDV6MgTagyv1mTIAuwPQWUtxZsnpgpT3+TybXqc/xpTsMJZzK/wUOEqVIy7yzvUjHIO+PEjk/J+W6afoMdV57g4N5ZmqTpPt0XFI38fpmvasSTOK5yg1P0gqbJ6oVze8iyZIygUAZvzlNEPynfVzRT6glUgOw7NQQrhEUJdVslwi6MqDJaHuGYm5n5vmyvgEzFMxDgEEBTwUs4JGd8BDUa4BSWNf8kfFRdeONHMiWzFCqy+5dyMudKOLFlNsUoY+5uFvchGxP2bl4W2xLVT86U9j93MMBp7puVMhvDfGjDdK9xtWeiLm3/UpA4eLGD+YV6mmyg/NV+lT07u3xBIiU87pEuM/4Hh3ogLkbGkpjUlttMqy4UHRywTI/hhKSULLhQAJmtrkjm8iOInr7GIdGvWE8c+GW2GXZEHW/F3qYb+kpP6nmgs5inRKX03KlE50mVRzOBcy9WNhfiEeQnA0Dpp4ctjmMBs0xd3kGOiHwJxEygKvYMe0="
        );
        new MinecraftSkin("googasesportsog",
                "ewogICJ0aW1lc3RhbXAiIDogMTY3MzIyNzUyMjM3MSwKICAicHJvZmlsZUlkIiA6ICI4Njg4ODMzMGE3MGY0MWQ3ODc2NjQwYmMyYmQ3NDllYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJnb29nYXNlc3BvcnRzT0ciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWJiYzE1YmY1OGJlZWQ3ZWY2YzZmOTlhZjQ1NGI0ZWQ5NTgzYzg5NzQ4ZTBkOGEwMjM2NGU4N2Y5OTVjMDkzYSIKICAgIH0KICB9Cn0=",
                "tn8FdlZiTzhFI3xFsyqfYu6VfwOKwuT8PW5a6NkxfEtCwpPmGi0q2u8QWcEUmiliDhVeskXPcSavN+m9H+mlWdBkT0WLckMnYGGy9CdSXhcag1iWM3rpaNtIMTUItDb7LUZw5S+fAvGHkBe3ZkHg3aPrL7PnTpnHa0btzRbZCOoYIagNUEQh+EVZXFPDsJQhBT4SEJDgWIhT2xsNhfEmUQwn4YRHVvGFkCAPPIEO3Rr2Xyzxsbd11UfrX5TqW016pXQdVnX6OASio7V36x671xGPplzrmWCAAv3e7QPLwg4oDLvZpYJcYl0bueN5SJ50NcUL4M7x6OPf0zC82M1fBR3CnC+37P6kynfE6Ldqlh9VA25LGmrshkrwNNQLb0Qww7/KtWZNBu171KdxLkAbmLLlCdeIwBvQIpidtBXXbgik9scnrzjosTJeb1riOcZfn4Q7mIIDznEJqhcQtXONh09s+akeHgcJkHLS03rWqVA4WOwHk8kNTfZWsm5olfRsVfDxxvIdlxtydHRO5KOSy6S94QXrsGDsWDZBtAt6gtVBS9r2qgkvmCcSk2Rp6ZwQkskhfnF/qTUflqcklS1mhQN6n5mpdv70xerwZY70O62aogJTdTEIqm7MtA+JEk5eii/+Usn6Owa7dgI7MxgWO0uRm9R4Tk38q9nuQXiLQVU="
        );
        new MinecraftSkin("Whyplay",
                "ewogICJ0aW1lc3RhbXAiIDogMTY3NDk4NjY1NjIyMiwKICAicHJvZmlsZUlkIiA6ICJlOTEzZmQwMWU4NGU0YzZlYWQ1Yjc0MTlhMTJkZTQ4MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJXaHlwbGF5IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzE0NDE3MTZiMWMyNjFmN2RmZjg4OTc1MjgyM2Y0MmM5YWIzOTkxZTFhYTM1MjBmNzI3YWEyNjQ1ODEzMDIwODEiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                "OiMBUrHAraijW6GPIYfQ8ua7lCDDtcMrWaZRwQuW84kj945V+qh9Nefps9PrMu6zsDB7YnoVnh5flp5JD+pdmjU/iSrTs3Q37o1EJ05kpA1vAp4jWR6NWGxA14HDBgmkbpbyr4ra8zmDch3sioUMh+ecIaW+eRPDsutbGpPM+8tMA7vbTjfZsbgf7Ltjv3TknWdApv1HIfWheSlurHOQ0PTcJ1WniGJEFAaTCdpG/6idY7lpgaJ45nAWB3C7KTuurLnPJdBg2M7OS9ByYbwz6wIXeM1PewUSrUNopkDfvyYgp8lZiC52xvnpmzZHvZbHTEIkYjAdXlrEPL41nHsKTYfENwldBDATw76mFPgAi8uuVZsv4YBpdJqq3n2ejy+srRHRh8/JSFNnXDvyOzAYu5j2bzfvc5yUhcJmvtQMY+cXzodY0nPc6MvK5YlVJfplW1Y57SWYuO0FjJrOwM15nmWw4xUrfweBohyz/yNXpA9IyOQSwHg5tC8hpK+HH+emWGmAocCeHk+vhvCGeSeqkBlHuLjbPXf5w0lOX5Rbj9iPrXPm8kcCMR2fqKAP0y5j/8nPnyo0dhUiwOvagpy5XR696ELLgiiDLzbf9sJc80dWr3wrvREfwcKFVfoSmBmngDZ8K1/zGX7NfRO+gYmhNaBgj+AyuHGwUxX4Ro1kbs0="
        );
        new MinecraftSkin("BHunter",
                "ewogICJ0aW1lc3RhbXAiIDogMTY4MjI4Njg1MTA3MiwKICAicHJvZmlsZUlkIiA6ICJhN2QyZTIwOGU0NzU0MGJmOTRkN2Y5NmM2ZTEyMzhhOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJCSHVudGVyIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2JlNzk3OGFlNTAxZGQ0ZWE2NWExZWZiODY5NWI3Yzg2MjI0YzkwZGQ2ZjNiZWVhOGMwZWRmZDZhZDRlMmY0MzIiCiAgICB9CiAgfQp9",
                "OqC4ihECLxxgImx4v+EnLX86Oi2Uxysit3Acf7MGTY/w5LeCW63sm9y2dFp1FY5Z8pLgca4sscT2s25cl+ztsy1fIAmcz7Mk5XRzA4iKfQXb0J9PJ1VK9SCbc1v2bDunrZyqfxk9m0xCk2u4mJOD1EGVGwNEw/yHwPYJ2hkKaxLDfOcvNlRjuUk5F6yKO/cskyqjsaF5rtB7jnCzP8S0cE+Ot8u97x1XAtFkg5D1CZMrUFMLp+HHDW4DB1Q8OFE2ktiPzcuLDKBY7wFpsUkYxD16UsZF+BjGOy6v59jlGR49OCNtM1XTiQJD8bTBGieoV2Hf5zkuPEBAKecaD+PxWkA2wqfhHxz66RFFvtDCuuMMVDSYuYIB3QJhB1Xq3nHqS3wKytfKhpLwRMR7FCx/+GhbyQpU5PLdGW2IqtzyNPEr7boQChWr71QA/kEumIeb7NjwDXvVI5yI5Qo+DSGocv2NJfc6Z12FuEIYleMUSG8HZeStKlrNa7xB1pg/yipexOulViJv3S64kplGyfWXYX+eVGiHn0pLFcO/YLt+zPTDw/sZzdjyCbrhXndWm/Nhcum+FYY1/czomO4gCoc12YRIyE32cBBGzPdtO04dLXoMR9dy49EXeZ3+K3l3Ccup7w/TTYESXQxWN7+ahqQVBamjPLyVBfkWJ6v0kde+YBo="
        );
        new MinecraftSkin("PayForTruce",
                "ewogICJ0aW1lc3RhbXAiIDogMTY4MjI4NzE2MzM3MCwKICAicHJvZmlsZUlkIiA6ICI3Nzc1NjZlZGQ0YWQ0Y2YxYTRkOTBlMzc3NjkzNTdkZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJQYXlGb3JUcnVjZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kNDlmYWNjYWRiMWViMjA4ZWQ5YzU3NjhmZTE3NmIzYThjYzU2OWViZTg2NGJhMDFjZmExMWM5YWYwMDM2MzdjIgogICAgfQogIH0KfQ==",
                "aA+G0hTjU3kMGBbPR8+nVw3jIAWiZe58OAXbiJOb2FyzWqMm1Y1iH9LoLwGQjkpV3c0P3sgW4nJo81NzdjadxM4byqIVRW/JMNU9wOikzV/mPPgIALavIDPAXomZdviN6Pu6fRLGqk1VoU1g4ebcHAKkHiw8QwMkhXahSN3Uwz0qDzYsm++ElCGpawx2b/sPDEMpEaihceONsxl7F6fyuWS/DqivI/SiMHwENnKZP2O26IJvQ6TWVgUE6HP1/+9saVrpQ6T3Az+0c1JmZrD1hlGdFrHofOtiJaS3TscvRbwjpyNiYk9OIx22UldRW5bXKghpbSUxed/KK1g2fRzkaKO25IFW5OLdft9i1AmOR7c2AXXeGxSklLlHUW9w2/tpMo7h0mzSxMLFNmLMv4WQ30xgjEnp206AHE3CdOV595ZIoNyN8ljORvDF8iNVs9efWBBzNNowjZqA9oCP+NNOxiu0VoRuvhoDoon42ke3zUeB9L/gwP2LaCcLpe+dArzWFjgfj0+jkyNpTMYZCAjlQH10ul+AQRJDleJq4GLCcsBiRWL01/XXJhUn6oyFhG6B45XxNGY2Q6zwfz/U6Q5RxamlloUNzVHlXOuyHMIiKAWP4t5Na4qMNgWvnZ6Xh/GMX9TcpAJaujp+Q2Db5ZD5gW3WjeZ2oipSg2ECcUscOdk="
        );
        new MinecraftSkin("Fishduper",
                "ewogICJ0aW1lc3RhbXAiIDogMTY4MjI4Njg1MDYyMCwKICAicHJvZmlsZUlkIiA6ICIxZGI5NDZlNmVkZmU0MmFjOWZkNmJmMTM1YWE1MTMwZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJGaXNoZHVwZXIiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQ5ZmFjY2FkYjFlYjIwOGVkOWM1NzY4ZmUxNzZiM2E4Y2M1NjllYmU4NjRiYTAxY2ZhMTFjOWFmMDAzNjM3YyIKICAgIH0KICB9Cn0=",
                "FFPNa+VuDsCkmLTxkcUk7aUBk+ZD3fzo4INLgBBl8XOHjkwD77e/YrJD9vHSmgdoyHZ92QWwoov4odzPHz7JfVr4CgTZjzSvj5sQGosCkxhR84j6chI6Vk/fqNzzjo+rchACF7mXUrsJrm4NWhPj7V+gWdAYri0xYCmUR39yhW6hvUpc8tSimgiMm00QjzZrenWOv2Hj5kPstgKFeFMijVWpyqK05IE80slxHz1C4+m7b197yQL7vPTICrbyZvqp+KlJQ6z31cBmZM4EbqCexAfxzRAhbKWK7J9ILYb/0CvGKN+jSUS87RQlSbfA1KPeDX8akVJve3Z7qDbHaAAq73B9vigWsjZ1W/3UXeMrjq5oi5h6WFGRNrnmUQUr4jcrVUBk7JUTchys8VNdfAGu3Z1QqY4eFQjzknXJKj7ksDG0qk9MWZ9PxESuKCpBlaYQktCKXIe9bSM/C+9Gt28hNFPvY6FIiZ5NUcXuWaNK3DFVOnkXPNFddrgPLCaUiS6ygrwCL9oIvwsqvmnME2LQeovmAceVZK9EpQ90Dstg/QzaFXa/3IEv6xpXYHF0LO6LmUoGpPPcyYo3acp9gQA+YqAEM0kILV1CaM/Ope5UfBNbl8p4KDRRAhZHU00nZmFaj5zSWRFm1dm0fCiMuE2hSwsLBygetBfWg67fj8sToF8="
        );
        new MinecraftSkin("Muruseni",
                "ewogICJ0aW1lc3RhbXAiIDogMTY4MjI4Njg1MDYwMSwKICAicHJvZmlsZUlkIiA6ICJjZDIxZTkwOGQyMjU0Yzc1OWFkOGNiN2NkZDJjZDc5NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJNdXJ1c2VuaSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mOTk5NWU1NTY3YTdmZjVjNWI0YzExOGFlOWY5MTIwZGY2MmEzMmJkYzc2ZTAyYTM0ODExODJlNThiZjFlZWYiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                "YPGiD8R1xZIU1TLccDatYWJNQ0k+eld2hllYQd/w+Ai0ZXoLlMNbho3q5AJjVR9Gwg1t8ZaAhQfACRbDi2gjw4rlQI3yzGMyD0BF3F+JRMPIhtwYlrNUKVYWlIXQwl0+mbjjr2ni82GtlKRUWc2coS5wC3HOtjcNIuu5YRPoWhMDvZVwP53OxchPQLClnx8sK0FjArWOrPgBG4LcNSE0ttz98T+cgym7BAbMQbhJdBh1JCIQbHkNNhH+Sz/CfJiKD1dCdFDecHnAQA4Lt0ikfiT6Un55PrmOkFqf9FQp5VBAjvwJ1b1mzZvWVceq0dTgbDuw+ikfst+kJ7U+kq1jK7inzMoFbufSkgt8brnyTte6/OeaobbmRIOFykbwLGV+kfDYWb8gHKUJDhrzD0Rh1oEzjSMItDve2afYsuKlILWcGu+50Kp6jYPOG7w2TlF9shMgThvnAM8zHhPFMZPDt6zcHdbv4lRg536YD3F2vqfNMGpGNbPp1ZbV7OwmazEquFJ2Wiy+ALZMNhiEjz5LDk+nXOWF66fXEVYMbi4a3PcU+Ay1qlWgOD6POe3O6oL9pMuPXY3Gb6XWTWfOfGUmN8XnXRImibwQeMS0RtsstaXwB/8Lx+iUvHDQlwr/wGoWXYy1azOl3P9u0uDc87vtnvrQxA4m+o7Y7Q9cEXqSHyI="
        );
        new MinecraftSkin("ObvEndyy",
                "ewogICJ0aW1lc3RhbXAiIDogMTY4MjI4Njg1MDg4NSwKICAicHJvZmlsZUlkIiA6ICIxOGYxYzM4ZDhlNWM0YjA1ODkxZjdhNGJjZDdiY2YwYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJPYnZFbmR5eSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kZjhlY2I1OGZkMzMzNzliYzk0MDE4ODEwZjA1Yjc2MThiMjgwMjczNDNmNDMwYWExYTEzMjhiZjFjMzJjYTcxIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
                "BL/hNncZ+CFnb0SKfa+euY279qTUU62kMO/eMLnoh3OJg4M8P3qCLybgKpPS6ERZdNrVgMeCy1/2f71RtlhijQUyI6Fi0YsXCY5R3y8QDrx4fcgeiDEY4BDu52/2tqwbr48I6y1JM0j5nJYmf88aLGdPIUxbu4vYbzOG72lBRAavbkvyLNI9Dk61MTOjy4tkKG0Wjz3sXuxyS+Mh97/VsG14mztJUNFdRdzUjUrl0+Pauwmj7CnZ/mS/3MEzSAaV+Flk9kF0/xHJqSs40BoP++0G1sFKBbFOjejbnFHx4z55QWEdZPYYUiFrlCz7SEry9e0Wop4YwOhc/Zg4XI44F0TShSuWRTpTxzGNFPYvLLTCFe5qIFBfdyIxDhHeNFp4Qbo+8EFmbdzb8HwdOCAVVSLfH/Byfs8tO1twjKyZM+x4L5A9xP2MetUILyEDbwJbzsT22n8y28d2WAYrKrodMIwL3MU18H0ozOAdPXvtVtvDMd7ApWTe/bcq/B9msKDryFCQcioAFEx39DR2NNMTml9dzYuVqN4mcnxfHK9ZskXOh3RVMYJTFIhLen+m3sCUegIWtCneiWKFxj6p8D74kTG5YSQvR52MHTZ3CwVjLyYdTZmEKvoeDs/gBV93lJgiUGE3FEWXC2oJCnSN93HezdofqpcTDFOPJAf+RL6na7U="
        );


//		Darkzone
        new MinecraftSkin("debrided",
                "ewogICJ0aW1lc3RhbXAiIDogMTY3MzIxODEwNTkyMCwKICAicHJvZmlsZUlkIiA6ICJjZjFiZDI4ZWUyNjM0YmQ0YWIxNDgxZmM2OGQ1NjQ5NiIsCiAgInByb2ZpbGVOYW1lIiA6ICJkZWJyaWRlZCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xMzgyOTRmOWVmYzU1MTMwMDg4MzRkNjQ0NjRiNzk1OTY4ODU5YWUxNjhiNDdlMmJhZWNkZWRlMDBhNDE5MmMiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                "JX9aUc40UpgC7G3uP8D6plpZnS9e/VZCfY0eB+rNaRTWLDqhrk+gFEGJy5bHvoszWuiJPVHa1o3z7fcgz13ChJh+8pOmBVHv/ji2yUYHBreW4lM02hEBlmFsVyiDmoGsf8g4jj0uuKpAQhxQbeqAJwQnZLWNVXFwHQ6gnqBvMn3PTLqtV+Zg4bjayS6jcnFgvgvhhk4mzlMWWJKlmAFvXm9FBe1CVIMFYEWRycih8kpGXzHNjBpuQ2SQnbtMSvQzs1RilDqD6/4wnVuQhgop0t6kq1mUN6AVLLj4YazK1fX6ylYRzamVMnsU1mrTXWKTp/0T88WAbhI+OQ6/PAikqigtROANrM18f8cF2CLlb2xtcG4aRKEtXGSDSgAU1llT16k0wqjJB8AsOxFWL+vqbVqgBMFaxM02InJpdoKwQ+fobeVy4cZeHgBwIcbeH9LqgGAveKfW4C6IuipRJM3WNwVHv0n4mu6nXV/11EgJEoA5v64W033/P+/N45NNEcz8WGC1Yu3Mgb47VCY6j+Zmwb8CbhJYLb8LxWH1jlt0UQpGeppwTbNEQDYIZqtf9KSUmeV025Y54+03CLntuu4cXR0Y7xrViR1VdbS5gV1HN7EfC6uZINEYgtgcVlwdjre1F5OkyxdgA21e3V7oMCz3d7I1hxoUBrcl+dkCc1WizjQ="
        );
        new MinecraftSkin("Merchant",
                "ewogICJ0aW1lc3RhbXAiIDogMTY3MzIxODEwNjU2NCwKICAicHJvZmlsZUlkIiA6ICJhMzllZjU3YTM4N2M0Y2QxOTc0MDU5YjczMmViYzdhNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJNZXJjaGFudCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84NjRkNDMxNjUxZDhjZTdkYTJkOGY4OGRkODIyZDk1NTdkN2JjZjliMGI1ZDBhOGVkMTNiOTg5ODgxZmFkYmVmIgogICAgfQogIH0KfQ==",
                "gJ5TpCfjwCkxvkd0wxelGNplAReFWQEOr+LBsuP2GPH4gr7GLldXk3DIUBwg76vyIKXQYxoNhaQOEKrzMnaaoJxt2yJe4YrCfjsaGPuQh20MKmnkqvadrP7depejTnIuiFT4e8IuelCHiGjzlZtC5lVlfX2E9ybAtLz6eoYSD9e69OI2CvUDika3n/Y4CpOLC+EEDfo5aEvQwYwHn5tEyyTiWxogAK+pWHItwYEnbNsunhJNe1zCuS42Agaq+l60ukXje2nreoN+/rypp7re/Ljq0oXutNGNJe+a7SY4jt8StmUfLLVssqWcVX+/0k22BWNF3wkwO76KdPPtAnhv/dd97yDzcRFN5tYD7NHNU6Y471g7Y1gQ4gAkgsWagCUM/UEKFzhFVBys/uyJhGBxR5ak7k7j4c0RmVPYR0j9skJbso3jMc4fK8UYqrcf1gSxupJ4hN7/4k9SdkSricZC7jD09zKP8lIfTbhxXylTgwcTuIZQMDI7uUWQW5UO3/+2mg9oNP87SdQVetq+ULWEkPyn0XU+YTrCsIkO19VCEu4gm7W0MN6TUwoEJ1WKu8Ju/3jMIvVh3K0/g+oasY5p4YkR+iJOeCBGoY34mbeP7w1pk/ltJwpzL/UiUGCvSewpY6lTt2PmjMKIuuX7XGgLCxnoXPu9ZE1Snu/MKD3V1As="
        );
        new MinecraftSkin("Wiizard",
                "ewogICJ0aW1lc3RhbXAiIDogMTY3MzIxODEwNjE1MSwKICAicHJvZmlsZUlkIiA6ICJiZjU2OTVmNmU1NDg0M2U4YTNhM2NmMDc1ZmZiNmYwMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJXaWl6YXJkIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzJmNjlhYjJmNGU3MzQxYWMyNjI1OTcwODNjYTM4YWQwMWJmYmJmODczMGZiZmJkZDEwY2E5MGFlZTMzN2EyZGQiCiAgICB9CiAgfQp9",
                "VV5XDC9j7B/qqRMvjAZ6ODaFnyQe1DZudLSJDTSfgqpFZSvVh3bxLqWn1kvEb8XinLh0WdT4ajyoIgZgzqgyry4VVanIuPpPjiUnezV5IxTr3q2mxFof3kRbT8tXGpIEsHPe5fNoTmXpQROiWpHKEKk64xc/xDZvJSKVul2HCAEe25JEDO6xoz4L0OVr05ko/kiVaayU4lbg+ThNGqTi4gfprNWnM+J01hCZnJwkxxOsshRJ8RHBRg/k4FjUFHc0/2UvMTSy4gZ/b9yUb4fr3qKjzrKIkIjLFD7NvQR0Sb4IDgAusw7ssh5JWYn0gAENVLSZ6CA7akqKpebj+WJRBSrhaGFMHi4dP0zYCAfdn8MVIIH8CyCYHjXwAeegGSE77mGZ4y+L/Awuevx4dgebVaRxQ6a6CrHk661D69jNH6VFmXJvggzk5Z0zval88egbaGZdHFQzL7o8ydl8GXaBFcBvKk7KIpoHJQN831StlfqKtuyCkoLiToAMEwmefMZ/5NASRJX7YBBo/padgjKJtFsb3LX1GN7+lEM1xJjpuOvW6L0ZzNDW74gOg/cEs/qBWcZcBAajlHsd44I5lqBIpqRqokH/R8G51m6oxfsFss6l2jwn1i3lR2RO9ZypS9FwfGXYTrVeKwDARm0NwSpY8RnsAlkYiIaf6dpibffDdrw="
        );
        new MinecraftSkin("Itz_Aethan",
                "ewogICJ0aW1lc3RhbXAiIDogMTY3MzIxODEwNjQyOSwKICAicHJvZmlsZUlkIiA6ICJlMGM1ZGJjYmExYzA0MTFjYmVkYWNlODUxMDdjNmQ2YSIsCiAgInByb2ZpbGVOYW1lIiA6ICJJdHpfQWV0aGFuIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2IzNWFmOGI5Yzc3NDYzOGYzODgyNGUyMTc2NmIxZmYxZTA2MTFkOGNiOWUyOTgzMTRmOWE2YTE4M2UwNzEyNjAiCiAgICB9CiAgfQp9",
                "hvVmF3J+DnIqhpSASStSdJ6gFIRdBXhrLJBITfpB6vxLh6NFloqDiIJAUB29kkwm/oD3fCkBIiosn9LNFZrmimDeWWkqpOXP28nDOPn04m8/GSzZitW2LP4Hp9qTaeV7zEEQ0kJKtrh0IQq9GWdlBc98D2J6z4CJmnXyJz6tH5ShTyW8deJQIIUcmulZlvK4RgDnBQD3nHLbQUT9z10A4noqEZXlN4Lqx7YPMpSw9iW26hbz2Heum5YXq13pk/UGkBs1Ppm9xhdTyCw8zLGwAo0FQe6bI0zDPHzAlD3qqC5TIszNPLAuO72cO9JgNX5IA+iHbuZJ5KqdDblNpGU9cbgJBqIl2n+FdmxLsm0xEJmaZ2nTMOsnKypGaU6lOQj2SH6QTUEixTElctskHfv45GFFz0dvQnVQ9PfhkVjMcKVHpv/g35PURXZfBwIqdqYke0mEwqjaLOJZ9Ly+M8XKTwthPTIl9ndn/C8yYicTW+Z/b8RQokRABfze82zBTPfw4t24UvnwpolkKIsntD30vQA6IuB72F4vvLY0cuocLKnorVSncFjpCI/rsRzir6D1JPknW89aRrGa57ABQHwjCILpBK2n1bG8gD71ybbxzNBoHTI6SOj/NVh5s19frVarNQyKWPgCtusVjiohbZGFdDeCtKyHpVpdNPz9xrEByAI="
        );
        new MinecraftSkin("Mailman",
                "ewogICJ0aW1lc3RhbXAiIDogMTY3OTY0NzY1MTYzMywKICAicHJvZmlsZUlkIiA6ICIzOTRiNWViZTM4YWE0NTE0Yjc1MGUzYzlmMTQyYzkxNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJNYWlsbWFuIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2JkMGJlYWExY2JmZTM1OGI5ODU0NmFjOGVmZWE4N2IyNDVhNTYyNGFmOTdiMmZiMWJmNzcwOTNjMmRmMmE1OTEiCiAgICB9CiAgfQp9",
                "rIGRyCJ8W0RS8xYSyhGSR6HTCMfRWxPLyu4E+CqEns8L+aRpdIYJhJvfzw5d7krpZRMeEAaKI/MxRVb/tUZ0gtpbBBTrkx9yceODdrKzhrx2g9qfFxOD94Vla6AfYyxQeUmUQHu1LFsL7WUlAHSABvf1/d8GUUv2vBuXkwK9g3D2oWvSro7XVY9vCJiKEZsFfnLQebv7LRIRkTmst+CluKJnEWrL/NRsiHEfkoZhc3yWfIShQGzNeJ2b5QtlsQpaSajKfnSKNYSY8iQRGpk7oc1s7OheBG/6pxIckUlQhW+KThvHInK3m1QUlwKqCAnvOuZXSySCrTzlRyOfM8Qxv3Ddh73j+290wuHriUugp6Jmg8bN8J1y5pchbh9EjJTcH6dziD8HTYX77W+BCc4OHXIvNgEaMdAgodkOYC+NF+Tgo7baWiweeHrjjE3/eT8CTKfrLI7IkhHiCK8DrELDUCOxpiWaUV0QHt52kRa72pVe7LGGDEC7IBB1RBoJvj4D9XZRS9puE7PTpOniQ2GhLN+3fpJetuxZPg/amCmOhF3F+ToZf0tVCbq7aDts88YOBl1WlkR+VETaQJq7CdL2Gl9vg8nFbDbTFIUKxVBwmx/9KD63k7Y8BbZbChVVN0HgdxzTVJxKtnOhA+EOXqp1lShn2+Z9eCiF1+UA+xspdhw="
        );
        new MinecraftSkin("Banker",
                "ewogICJ0aW1lc3RhbXAiIDogMTY3OTY0OTQ5MzQ5MSwKICAicHJvZmlsZUlkIiA6ICI2Njg5MDJmYjI1YTY0NDBhODBmM2Y2MjZhYTk0MzBmYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJCYW5rZXIiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmZkODNhY2NhOWFmM2JiYWQ3MDVmNzE0MzU1ZDk0MTA3NDEyY2E0ZWJiZDRjZTkzOTE2MGMxYmUxMGNjZDFhMiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9LAogICAgIkNBUEUiIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2U3ZGZlYTE2ZGM4M2M5N2RmMDFhMTJmYWJiZDEyMTYzNTljMGNkMGVhNDJmOTk5OWI2ZTk3YzU4NDk2M2U5ODAiCiAgICB9CiAgfQp9",
                "GkxG979OI5NMIV+qvZIys4550w/lckSh1RTWuxHSK1SMxv1u22KafABcOwgW2Rx4tcWezy9S3RAqNNkLZTV95F8AVcaXUgZpbq1uduBJ05fsiNv7gkTvjOgyIbP+1nPJZG0t/3DNtKSNoRSKoe0bJ6pWQrhLX7Qw9zGbhNLo86DAw/DvkSrJTUbpPtRuXruUr8r7XBJWpyT3FoQNUOSs+0LR8H+r7ZoVcD944QJpk/87nbQoNzujP3+OQ/TPsXANzN6MX3YfGzT60iT0IJVaN3JwoQ5k02Oxs0znqwguxYjZOFyElIQglx+FlfI0I0e4/3ehIRDuQxP/mlyulOrTEheb/zjcI5v5XznWygDtwrkBGzd9nDtd8aYeBKbIBtvTGftQEwHLEyTTFH/3k5oSfSxvPLGw2CPIzeuJBjDll4/GlufhzrpXej01CBKi5XVpG6Ef9TxqIHkSv2+xtB48U3H/05ProqWGOUYzAHou0amAct3VaXgVF6bc8EpZ/1iuu2cOW8U5yd4ajVcZWWvVEydfD4EbAk630f7rmYbf4rxKSQhnIivKlpukWhPAJs/BWiDLhE1rYhwgr+UrB5jK9rfh9xi9XhGck6UAZoxbrC23l/rYmHJ09Wuz95zMR5eUsRBZkXd4t7nYch003z3sqn2yik7tYtpDViLFnNK/xLg="
        );
        new MinecraftSkin("Sammymon",
                "ewogICJ0aW1lc3RhbXAiIDogMTY3OTc5Mjg2NDkzOCwKICAicHJvZmlsZUlkIiA6ICIyZDhmZjJlNGFhMjY0NDA0YTI2M2U3OTJhZThlMWQxZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJTYW1teW1vbiIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81M2I2ZWMxY2Q5NjRlZTIxZDU1ODgyNWQ1NjNkNzMyYjQzMzgxMmYzOWEyNTY1ZTQzMWRmMWZmNzI3YzNlZmNhIgogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yMzQwYzBlMDNkZDI0YTExYjE1YThiMzNjMmE3ZTllMzJhYmIyMDUxYjI0ODFkMGJhN2RlZmQ2MzVjYTdhOTMzIgogICAgfQogIH0KfQ==",
                "losPS61TxzSyjLNLab+vOMhADl94g1RoymIvjHiCjiMRp4Cgc3mCwmgDyXtjxn2lIdIxYEdvRIol3ZOmKyIO8mSizWPRpDcTlLSzzljAmZRyfCoPr4Z3G3acYZbtqeo8eIUV3rJnkwQs9WxPIXNMbMaXN7QqcYwHNpI9VH294j/CLWQW0hPIXOxaO8tdk2NXnJ+7a4p1g3KNMg9goDunvrodfY0R4hAqGohVGe3l6EzBVFxKVXF97Gi3qh/+surCq3xGPv799PUXHHWXtQP4oRgsJ9RknU3t8UfW6Fcu8AlB6BMTAESoLeZR6xY70/sjJoN3DRu2acUoCeSutef9eqmYaaWyUDIkOWbVDRbx3uclPuokbpCHp+nLkZ+Xam74WUd7+JXRsTNJdlNXT7dIByN3EKF0d8aAhglIphqQOARPAvFvLdN98HlR8522qwJeeJA54aXmNfvorDsoFMsexck4i65ab/NIQqDFoXzKncwzeBFgDQiSg98OaX+4sGNJjge5VPFdaHx6rFNLs1vAjCmB7odmfpSPjB7vTSwgbQSTy6AlZM4AOZS3Bo7fZWPYMBCgvNEin2YgjVEqiVj3KwWBClZ7C3Kgl++9m8AIUQxLwZ17spkWctY6pNZhD9dqijfpCF2iHNYj5dbTsrOOprw1CIAAXAaKOhWQ89i+geA="
        );

        //Boss Skins
        new MinecraftSkin("Zombie",
                "ewogICJ0aW1lc3RhbXAiIDogMTY4MDI5ODU0MDk1NSwKICAicHJvZmlsZUlkIiA6ICIwMmIwZTg2ZGM4NmE0YWU3YmM0MTAxNWQyMWY4MGMxYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJab21iaWUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzgzYWFhZWUyMjg2OGNhZmRhYTFmNmY0YTBlNTZiMGZkYjY0Y2QwYWVhYWJkNmU4MzgxOGMzMTJlYmU2NjQzNyIKICAgIH0KICB9Cn0=",
                "rpuv55iD3q/Ez/LYNnpHKFerMm37D9RKprCd4TWEdhrxjzuzweK0kU9v8HeDYB3ZV3wmSnEr6C9yvcVbjrYFN/VKGb5gEPk2yEPHnEvn0XeoWe0MKm5QUH7w9pOxbf9lrKAg7hqRG1j8MhJ5YmncpAoQICddEknwOofjyJg7ts0GIK4UpTPeEYOKomvhq/Au60lZDJ8iZe028CiXudx1F764EynpZFUiGWYFBU7kb31KrJQTDfPsoQK84BOeMnfAZUzaAb29eDCHVSr3X1zuynRhYv1VsdCho/jLew+t34b5grtfZrzPJKloZnXpUdtGlzmz1DYe++AUHf2AtbGVe8BcW9SmRstePzQ1bjb0kQbZqShrhwkRvq2sblndwePGOkfxF6oHk8k39AdgrlmEKR2FYUJfxLuUChYW8EOwJhMQjkgAX9IsemzTRiOo7TSe25C1xRx9imGhfxGpTXudgqAm7atLk1KeJc2lHoZOA0W8X7z8bKSACBLgyDK9IG1F9/H3dchmEuI5xmyoOl2W6G5xRF0qPEEsWRoXdg0zvHyTaBiJ0Jv5RNALHCO8bYxkpmuaU3efT9GnLxarqAPI6iuXunRwdaGm6rmOKKmvnlamuzaMXSOSmjy9CYOvJVxgLnW8napR8UI8abRzJkoDth92x4Cg6z+9OmFxwWosvLw="
        );
        new MinecraftSkin("Skeleton",
                "ewogICJ0aW1lc3RhbXAiIDogMTY4MDI5ODU5Mzk2NiwKICAicHJvZmlsZUlkIiA6ICI2ZDk1OWZjY2UwY2E0NGZmOGQ0OWY0YTJhZTlmOGRlOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTa2VsZXRvbiIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iZjBmNTIzMDMyN2VlZmZiNzYzMWYzN2JlNTQ4YWQ3YmRiMzJjYTc0M2MwNjc5MWRjYjc3NDNiYjk2MTY2OTkxIgogICAgfQogIH0KfQ==",
                "Kr0Hv7dT1a1Lh+QeELO1WXH0jzOF07JdGuuevHvVCDnuLdZUI5teBYdi1nnzxc3EVTmCZTJbw1Hs8pfSHdf+qyW9WABBGPPZcSFxGWJ69puPR446/0jPpOutOClJanm7HybG41hrVaTICvNUKvYT1ZpyGN3WjvjEgfAu+2euBkWSfFvgJTFF4OTTMEF4l98KuJK3qxZuPoc9WqNNX8xifda57Ih5XRh2XKk692Eb1aveTfNhSL1rHyFu9rxADwuVt/3I6mMEUnfsGQhFHNaswMJpaDGoyy2E/Yx+sipzyGdwNEeWzuqPO0jUVArSzSeb7myEhc/Tx5D3hQSUGaqWkzMQvhpLM63iRoWYBXBNn/2IXwSTBBDqzuY1NUCkyCuyzKu63wMVMlRlMhgNoiFd4V8ebWER9gYw3ek/G+OQpWF1hPnBPemITjiovCuNpAN/InpCc+eb12srj0Wt3RRersQvZdVa40Q9bahvjhTCc9LyCybevWIiAo0l9o/YqRWy6+WW46xtxc1r9BH9T/NgkcHeAxv3B/H7XeM8CbnqXVcOEmRbzBew0gOzsNDqKGZB7xMvc1CdeyrQhFjUpH2VnGoPmErCft0oATiTD5jAp2B5D4t1QjZIYlpDhnE07ptmXeAa5cCdgAfatBSNbCjesTt+8yjGKwESzae00NReG8A="
        );
        new MinecraftSkin("Spider",
                "ewogICJ0aW1lc3RhbXAiIDogMTY4MDI5ODY3Mjk1OSwKICAicHJvZmlsZUlkIiA6ICJiMDllMTA0NmM3MGY0NjYzODVlODE0YmU5ZTQ2N2Y5NSIsCiAgInByb2ZpbGVOYW1lIiA6ICJVblNoZWx0ZXJlZER1c3QiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY3ZTgyNDQ2ZmFiMWU0MTU3N2JhNzBhYjQwZTI5MGVmODQxYzI0NTIzMzAxMWYzOTQ1OWFjNmY4NTJjODMzMSIKICAgIH0KICB9Cn0=",
                "CrJxFjUTF6vUzAKjxnt70aJwCJva+uJ6u9GkhV3lkHiP7+EE7yJVxkhPHNyeBUIbfsTCMpgxMSxnxxF0Pf38R213Pp9YL/HPgp4LujAf031TW+3iplGVWdzxkuH5dimUSc0KVEECpLPlBjTUqHxvSaeYiDlB9nTc/DGJN2THVL/bQeNL31B4V73wDFT72sBVahtpUlD8Ve+aCQ/ADAW+g1cVwbHhqwXcLaBdRbwJdWPi6FBjtf43ecD/VEneBHayCc4n7zl1KV435Pr5fnPdnh4BrkkQl/RU/UZeRXe/UbubeV5+El0HkFF2+o05JOXLBZFVeGd/uHyuEJZj6n4hYPDgTbT9p/8QWt/YsPbbdw5dt6FHVSQ8ywRzeQwLLIdbFZKOxpR72NvMQFwB7IIH9lynFrVZnGrYZ6tb+2HthC4Zc25+vEtVLGAudvuFAVFO/JD9BC7YuajBJwWRTrIpcHGCQwqf1Q6sM/jlbnLlzatzQL8QXNLuBw4Vt2Eyrl/US4wjCW44g+g3MoDQlMmlewhiPB9ncZD/z8Y3Qj6cSvr/8HgGMwiMAC69kLRTDeTJdYxaFdTiCAwNB+U3dl2atKhnBLQmcQ56DF4gDQ6bQJzlYnYqKXl798DS/9HLzJRjVGm2kzKn+ZsuculgdsAAJzWVZd87pwDJK9NyvKzuvrc="
        );
        new MinecraftSkin("Wolf",
                "ewogICJ0aW1lc3RhbXAiIDogMTY4MDMxMzI3MTE3NiwKICAicHJvZmlsZUlkIiA6ICIwNGQyZDhlNGZkOTM0MGY1OGUxNWU4ZjIyZGI0NWRkMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJTa2lwcGluZ1NjaG9vbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82YmJmZGZlNDI4NDEyNzdlNjBkYjU2ZjYzNmU3NWE4NDRjMTI5MWQ0NTZkNDBlZThiYmU1Zjg0ZDJiOTU3ZjU5IgogICAgfQogIH0KfQ==",
                "NXkRqWZR5Nvic1tOvhCfkLo7PLj5+s8YPb36T4fA0YNnzPfSmIdDnZVfgkhfLJx1/yIVkACUA2JcJWlOkAGgydsfICfgrsKJAnIS1EdgZZHXj8sDpiBdoaoHX2J2Ki36Met0RYgPM/1raVIJzm8s6HeyjXceN8Nt59wEeu5yjrg3W+dmRhYCne5LGawiqygcoMg7XwLI4c7+s0WDs6ibJx8juKNTZUfbmdH5CKchtyf4gKJJ2m3wsDzXuNVvT8i0YirPKZHsDQlOrzf06lhG5IMsZA+AIl6am43FiMfaltKO6dyr2BC+Js/Q9FGAgsfwoJ1vVZNUtJ6ZP78Zp/R2enaQxn5ppkS+Dw+GBEoL2eoeLgQK4/UHmM45/g0ENCHXHiFeUR6Jt90cp+xfrIo2Gt+52hteAdZK4VEE6toqrNWgYmEVGJWvA1gbVGd+uuK+Lf6/KccG4p6Iw5iM4KNML2pAXcw2oIDHcXQzogrhrPC4utJl7BFk9V/0xSN6xbaLh1ObcZtDz+xodF8RFVqE+vqnRy6nOV66td9RJGxdeXaOvaWqhmukxlZIvVXrz0KXVzMBqk/p373Hi3PjAR2/1XL6NhqR/twTG+/j0oPx3638UYoZa58JCEGZt+Y1FMpuc64Huauf0961sCmo2iSu2WD+Ql98JOEqI+3l30YkSJc="
        );
        new MinecraftSkin("Blaze",
                "ewogICJ0aW1lc3RhbXAiIDogMTY4MDI5OTA3MDE3MiwKICAicHJvZmlsZUlkIiA6ICIyZDcxMDAyN2EwNTA0Y2IzYjE3NTdkNjBjMTBhYzBhYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTZWNoc05hdHRlcjc1MDEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY2NjQ3MWVhZGU3MTUwZGIxNmI1MmI0OGM0NjdjODYyNzIyMDRiNDk1NzQyNWM4NTRmZDIzNzBjZTQzYjY3IgogICAgfQogIH0KfQ==",
                "HLPbMdjpINKcJU69yLLv+2IpeTOn5bDBlXNKRJizHyhkTmHk8LCgi95pCmuvrQVRimeKisM1UeDGmD27ZbEDojMeIlDbHu7b03yoItPuXmabyGS7FtRR2grvjRbDQG9P9d9anhwd/v4xMY6S9vdDXgEdB1DTGMMjS3MsT1ALHJlF/mE4BtjHWzMXK3LGU7ZWIHaAkgoKXgoCINto2z3XP21kqrpp66T9azB3WFOiaC06LnDR7+6z+XaA2qsOfg6WSPPAsGrtQks4IZHOG96wTDCPoortVBi05sHL9uNWgXEkMlObl8WPzMT/Uz3uWfdaEGhcf1T2P3mngC9pgItiYwLxxH0v1LGeSmVLy1qqHiTLtsKlFTGvEcMGRDzAAUwSRi0h9HUiJ2L2EJIVOzy4zuR5LChtnCtpPKxHfBnDYxwysp9dPTtcgn4yhkizB8fce7t6M1DwE/G7gOrjtkRyQh00V8Fjgo7KAQ6xQiH89BifThZkJhrYNh2Nokz3MJsLXP9dUPtl/g262vTdAb+0IswNG3UZVCxlhM+Bgw2Y+P0M2I2QRICFvpTjDevcq9HlBg/61+G+GaTtN+UczXHYmPA2mjhFSIkraa7ivept2uEUhZFWbjboLfyMInkH7MrRz5/EtxPavBZqPeq+DwiVXz+1fh0AYAxvEWJMX6UHW5Y="
        );
        new MinecraftSkin("Pigman",
                "ewogICJ0aW1lc3RhbXAiIDogMTY4MDI5OTEyNDEwMywKICAicHJvZmlsZUlkIiA6ICI1MDFiNTk5NWMzYzY0NDI3YjdiMWE3ZTRlNGI0MDZkZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJQaWdtYW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTY3NTJjOWY2NWIwZjYyOGM0YTBlNDk3MGI5NWU4ZTUxNTkxMWFkNDdlZTE0OTk0MDc4Mjk5OWRiMTc1NzNjMSIKICAgIH0sCiAgICAiQ0FQRSIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTUzY2FjOGI3NzlmZTQxMzgzZTY3NWVlMmI4NjA3MWE3MTY1OGYyMTgwZjU2ZmJjZThhYTMxNWVhNzBlMmVkNiIKICAgIH0KICB9Cn0=",
                "XEyfY7+D0bBEWlLGV6qzSPh/NiPCPSdcCVMataMHnqTKtM62bkADG16MIclo+M2nywU6cjf8A+W1O7UFwC9D9DplIdFhPvccy9oVF5bWm3QqjsndsLBvGTwtR/DiX1gWoriA43A0EE+nh2ZNfxy+w0sRTzsdF/2shddAvNoKdxm9/e7n/RDyhI9QudYUZnz4I3brcdJgTlxVPeVzN/xEjQt5uue2coTrtXe+9DuQbdaQ/7mE6w4k3dn023P+hHLWCdhqegiTvb9UI24Ntdbyim9b+9uVHNZCkL87Aa30Ca5IMWOoj148Ew1vwfB73lKugDhw0Dkar+7N6MFuKJvw6FOQqVemCZY+68MlHOz+O4zMYIVgRGsNKEVppqZBrH3wGZkD8wFBouu9lggXWnsGhFLl/k7LMc09ZdW1o+R4zMZLeUZ9Pf8A98Y2dp6zmU/JaZ/et/eubRoVtjQx5n1kFcB3BCHRa5uEwkrn5ZklFE8HQJZCayCwYNcNh8d1/cPE4P59R8mtdUwkJsK0cpF2W3djBByGMMG5/8kO21fT9jX0lD0pN7HAwzYKY6/bXHyE38dvemLzLQnGOJLUhwN8rVKrOvx2PPVOUjDfVkM+JNAjKOJICcjIQA/OY9xKVTOnMshnTePpcs2s50cwri9NPjArcr7qWR74Yygpbf1CmRU="
        );
        new MinecraftSkin("WitherSkeleton",
                "ewogICJ0aW1lc3RhbXAiIDogMTY4MDI5OTE4NzE2MywKICAicHJvZmlsZUlkIiA6ICI5YTRhYmJjNWIwMDM0NzFlYmJlYjBlYWY0MzM3Y2YwOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJXaXRoZXJTa2VsZXRvbiIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jOTE1MjM2Y2UwOWQ3MTRmZGJjNTAzMzJhMmZmZTViZDZmN2Q0YzJiZTI0MjlhNTI0MDAxY2VlYjU3MDNhNzg5IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0sCiAgICAiQ0FQRSIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjM0MGMwZTAzZGQyNGExMWIxNWE4YjMzYzJhN2U5ZTMyYWJiMjA1MWIyNDgxZDBiYTdkZWZkNjM1Y2E3YTkzMyIKICAgIH0KICB9Cn0=",
                "g3pU0GPk/c6U+k96TSZnmHcL26FFgkW8/l5ut8ukWChRiizQFxrFpcP0nD6BPQ0PdBE/S8mybNvMu8mWowEbmm2Byj75UNv/BFz4+0lGLgfEXTYO9rrYpUAgcuBImjsxGeilmzQs4X/4Nb3vqH88qcn3tAlgSH840DWuhGHqP87vxtKNn63fq3SYmzK0W6o+mueBucteCxVf/lN92ywG5V+DKbp9dN5Mj9zCNtR3CVwW9c1hfo2S2lkuZknTHpTZsXVzP7ElCuq7a7aeykD9Wrp2025Ojw2ByFpehmn7FggU1T8WVt6QJ8DA3BM1y/4yDxxnZJFUuq1pVi6qOM0hQLDblBXBO1PHLpZQgbyxwuUDy6whzqp+bL1no0GHuiBIP3Pe8KHRmCNtWVM4BvJyf8A8/1ElLpyFNj4wxm+mzYWPViDPT4Zi9PCHLw/TIqVD+xu2FmCdkRqfdLLlTkLYVaGHvrxLtg9xYnMWE5slPGBeqducbV9Tc9D8tFLMN77Gu0Lk+JQRKEk1NRABIFy1a6oDN+artIq/q1PpEob9muw7h9dVwT02xuYuky6olJVGsn3HdfG3A62JElK65XwvwHrWC+1yXCo9xEj24+3ltxHQFF2vMQqbrU2GthCUGRAn30HNQBElDRFBB/x7eJH1vrurw3ImIe1v80ir3iCu7f4="
        );
        new MinecraftSkin("Creeper",
                "ewogICJ0aW1lc3RhbXAiIDogMTY4MDI5OTI1MDIxNSwKICAicHJvZmlsZUlkIiA6ICI2OTY1ODFkZjQyNTY0MDI4YjU1ZTk0NTJiNGRlNDBiNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJDcmVlcGVyIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzNlOGUwYzQwYjBlOTMxYzc3YzI2NGViMGEwNTBkN2U2ZWJiZTVhYmYzZWE5MmFlMTQ3NTQ4ZTVkM2VjN2JkODIiCiAgICB9CiAgfQp9",
                "f+KUUkSblz7gnqrVHTpUFEbt7HfS3wUCqH4LyTiBAs57s4IFMlAxNxvKV2wzITCIAlNhCI8Zz4hZCl1LnzOlPmep29NN0KNHJrNo/S2XH/FkF/4UEttXfapNs4IXB5yrBJUlVDwD1v/V+wfaLGZEAZdw1EW7GDWVBf7WpG5aVknAwZfOrTyzu9e/TNADOzmWAyRKwt3ZaUfudy3ZJVJPH0V3knEWSd8eGkkL0CSfzGimdn7UXBrMnnn1Wjj5dRqnJGH2tX9AF8jkeQjO2dFIC/AFDGPhF229GC/uIxhCVM7/TAWz/AWOyOGdrZeHCGojf3dXTx1PaI/RoQtY9W/vXim1vfC4R1sqqURzKQzF2Dq3teYsJZMC3ptQFSSN+m76psZKylF+1JLLsmkZdQ8JV28NSNN9AUOy3MzJWa4EkDpGaJeFBPWSs1d7dCwWgW+dFG+Epbxxy1DmxOQFr/AFt8+zs7SxhRR+6naI52Ry2qx+ZqQER4h2rN7m858HoAQY86m8XiTf2y1T/NVw1++vE+5+2YyBjC4neAid+SgYiMgSYL9NtdiCPzxfaF6Abqb0CoGlqprC0q8X0YrYsu9gVpzkOwE6wGM78jtBPoXcbF1/qODo43fkDgi3A/LTIVkBDq9YUyR/SR0Z3Gy9HgXImBLdJjrT1UuhVxDDHWu3+6c="
        );
        new MinecraftSkin("IronGolem",
                "ewogICJ0aW1lc3RhbXAiIDogMTY4MDI5OTMxNjI4OSwKICAicHJvZmlsZUlkIiA6ICJhNTQyYTlmMTNkZmE0OGY4YjFiNmU1MTA2NGUwNGM3ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJHaWdpY2EyNjA0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzc5MjFkZjNlODJjMTAxOWI2NjFjMzM0MzIyYzQ2YzY1NWNlY2NmOGQxYzQzM2MxZDlhZWVhYmNiNWQ2N2UwYWQiCiAgICB9LAogICAgIkNBUEUiIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzIzNDBjMGUwM2RkMjRhMTFiMTVhOGIzM2MyYTdlOWUzMmFiYjIwNTFiMjQ4MWQwYmE3ZGVmZDYzNWNhN2E5MzMiCiAgICB9CiAgfQp9",
                "o+WLmkJILdZYJQeJwEfYGdLnNsumhYeJ7FvcDJWhKvPIDlsQelw7tGRqylWxZGDSmIOTfe8pHJF8vDxqNlzDUpUNheAAMliskuDNy+h168UM0nPF53fTuGYy1arfHejLnaEilohHQYZGc1N/nDiRzvdMk+d5AdsUo8RnPvdJKYw1F1V7+oEkMR9QwOTazZtr+n9QQSLTLVZZcWfU5ks4mX9t1jEocn2mgsV07Xo5lV4WMVsHPqxlnn+qt06Vm9o2i8EIrAuFYON3WzaEFmm+DFEjLPYK3/HmtaoGHDJ6wB8oSGTME7Ki/HF19LqmkM0XGHcufoSLDUeg9i0rIHsq96qRBungPfBU9DIOLwGKDEEr2zT54jXZpA5JNUJRNxmW5ns+cRLAdSx1VHIR5Yx06rJnCEYMAt7tA2ldRdNfj6o3px+5Jqjczf0x1MapfYhWiQPG9lp+iHpxcyIOBpzXVd83+6UsHmMu/7ALfSMYt092L3JAM/rult7akqHNiLGb27tK8bguTJGDp1hNzRG6uVRvn3fU/gOXBP/iaA8gilF5ep/zXBL9lUy3726fZ2jmpPznkEBIjJFPOE1874PGDDZTWQ91mikfF4CUXauv3bDUiFzg8E1L82YlE73U1OxReQ00Th4iLMSwm/C4mJ5w8WnjAoE/ACteyTMNLewwX3s="
        );
        new MinecraftSkin("Enderman",
                "ewogICJ0aW1lc3RhbXAiIDogMTY4MDI5OTQwNTIyMSwKICAicHJvZmlsZUlkIiA6ICI5ZDY3OWM0N2VjNTk0MTE2YjUyMzVjNGU0NWI1OTJmOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJmZWZlazEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzA1MTZiNjMzMTI1NTAzYjY1OTgwY2VkN2ZjMjZhNWQ5NzZjNmUzNTQ4MjBmNTEzNmIzMmFjODQ2MTM5YTM4NSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
                "lHUXE02tsEsb5CL0SiMU567rFbBydhigGft6EOD8EOGGMn6KMf2hlOSXz4/4T6b9Mfx9VzY6xbtSwxxx9d7PQWhhnWv7i40VUqNnqAYz9BstGIQoWpOybSQpoH3BzpIX8KtA3QQI7uElV4RtW7tWsHlGXI4CglMl3KtJOGngAabqYAFkLYOQBYcZ0qNnu4slobGeryD5JF1HBtrW7aLFZkLoiiw6WUioUIM29xXqScEZMnPfaDbfol0pEUaZ4dzbE/Bhyp0Yjbl31yVwWDL3H68UZv5nx9eY8PbC0vcCt+BBJMc1dFtFxWWkfBVyhq41uS9a1rrgAOt++Gg6OhWJOOIK/aE+cCDKYdhQuj1Z7GaZgaZfZfnWE327PtldHPLsPfdVpfDAKdn9xI9sy0L07kPw8e1drDUxwj8PWGKkXmCy7Zf0VFxRXJXutkqJgLLf5rkj6Q9OzpIEc3BSDXxDse+WRnJ6GViYzbSdK5giUsEZ6jJ4wngIEntF4LKaUUFDMLfaAfOzxzbXLI9iQMLYYwFc6xMNhFOHgVbwMODvNnskbwDdt3lxn7Taf50EqkNJJIEByUIxIUqmVEO8I1PmIrHqpzHABaxs90LzsFpRPX6OVEerxcuHpXyUmLTEYFF0p671ac4H0KrB3+8f/eSU0P4zI8wPkYQZHIerO1aT/NQ="
        );
        new MinecraftSkin("DarkzoneTutorial",
                "ewogICJ0aW1lc3RhbXAiIDogMTY4MTYwMjc4MTg0MSwKICAicHJvZmlsZUlkIiA6ICJmOWUxN2Y5OWMzZTY0MmY1YTkzNDBmN2FhZjAwYzZmZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNQUtDQUgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTIxZjJlNjM0YWJkZjVhZjA0ZWRhOTg3ZTAzMWNlYWI3M2MxZDJiZjA1MmZlOWM5MjkzM2VhN2RkNWE1N2Y1NyIKICAgIH0KICB9Cn0=",
                "TkpO0nIBp+mqnpn8YcPSo75RrdqhbflnScQV3j+TcQIq8RR7b/IILRqh2zG1JThfwMs+l4Ng7YgRb0S9GnmT/ribtLks6tUebQNnstthtu5E2dlrXsVmYxaLs6peMpuM//5Eh9Pp+vH5Ycb3mz2+KcUnYvfiCkpOPsMq6P0zMmkQtsDYFueMru8czVrvSul+9S6bf9VLJGBZ/cBiKRPvJlUG6zPSbvGWXTT8CehDlX8hpDqzQknCj2rWXnnwjraXZeBZVYFqAUOX5avA/bkzLq2f/5q62BR6pxnqNLSRt57Lq7E4biv/j4cxxB8eefTc1aEFfSuAm1WAwqhaTVK1sfNmDVKA1KZuP+UxLQqHlxwYKYV1ipT7nGnu5/0uxftMW6iVYiZMpG/r/bZpPTrXRxsVIXpGMdNxD647I4pDXfuh82EwPzMKEEAnrfSEnpkKnAQPemVHPtkIo+Aa2VsMI/KoE1SDdlezJbjK++YMK6oh5PQ1mE4p6MQEz4NjC2IUo6TE3vv7wk4KonbfcUOPXK0SRfCVoJKRtUN6E1vk2AmXHutD0UY1gtv8meDPlAvbPdM3w7Zp0B7uLnKoXvSRP9vseoARZw2cb5peLIViZdBjL6Tv54IUMRIso3BI5a2ejy9BPaTwqK+qAldXe+reHbpfZzx4gjGmNLmUEjBySYY="
        );
        new MinecraftSkin("Reyertic",
                "ewogICJ0aW1lc3RhbXAiIDogMTY4OTU4MTAyNDM4OSwKICAicHJvZmlsZUlkIiA6ICJlMDgzMDFkYzBlNmM0NTJjOTkwOGM1M2ViMjE0OWI5ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJSZXllcnRpYyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83ODRjNGU2YjUzMmQ4NzczYmUyYTMwN2M1YTlkYjY2YzNjNTkxMDkzZWUwNmM5OTJmNTVlNmI5YjQ5MjlkZDc5IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
                "omOlsZ8klr0c/s2MAPEsNN8+W2WOhzmXUYqAfUpYbJWstI9wfeKPHf3rUCvDocfyTYAXSpWyeDmImysaOVV0nV/dl4QJUtgDjsSdOMYdn32wpzXg01FhEGzwgdkQEA0r2dtliomHg1fbJTwiPxSVF0JUHcgLCsk8k0TvXs9pNjN4Z0c8IBy+gpmHotxOaqLPSI/QjjsBQVHQH53oozGH1WULa8CObeOR9hoQMgy20AepfD00PLOQpwMXkixGtu/Rx1Oj11WMcOXFvtCeBrfy0YzYkq64ZttpU3C+tPq/jGqxNnfYqdMYquiqpYFBDelzYHLN6pQ1bhtNKv5Gswh4qDfiKbL3tJ4TI244t631bTFPmDizChQLEpzjgvgX6SnQYO4SjvXIJx5CDUJk046I4sBHnCXJA7gdjrnn+eU3rKEJXfJyaY65/ReuIBrAdT2kU2PCE2nM2AZb/3bPF+6UDZ3tBhCUzs1I3lynY84Icv6u6nmPvFUg45tNJeA6Z471cQMj/cDB15H4nX/rwr3i2TuMpBPd1+YZU/ZZO/THhJIDOz3QeZQ6cOQsnKzGAFLEbTidaK8SjLiOGzIMAi0XfJ2CnhLHoPyAYK4UH/vS1QwwqcKbYj7IujLP4THk5S3WkRKx/izLwR8kAUgKV07sgkSJfHAWkW2ZdVrGGNGnDII="
        );

    }

    public final String skinName;
    public final String skin;
    public final String signature;

    public MinecraftSkin(String skinName, String skin, String signature) {
        this.skinName = skinName;
        this.skin = skin;
        this.signature = signature;

        minecraftSkins.add(this);
    }

    public static MinecraftSkin getSkin(String skinName) {
        for(MinecraftSkin skin : minecraftSkins) if(skin.skinName.equalsIgnoreCase(skinName)) return skin;
        return null;
    }

    public boolean equals(MinecraftSkin skin) {
        return this.skinName.equalsIgnoreCase(skin.skinName);
    }
}
