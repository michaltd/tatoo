package tatoo;

import tatoo.db.DBFactory;
import tatoo.model.conditions.CalculatedNumber;
import tatoo.model.conditions.Condition;
import tatoo.model.conditions.NumberCondition;
import tatoo.model.entities.AbstractEntity;
import tatoo.model.entities.ArmyListEntity;
import tatoo.model.entities.Game;

public class TestData
{
    public static void createGame()
    {
        Game game = new Game("TestGame");
        System.out.println(game.getName() + " (" + game.getId() + ")");
    }

    public static void generateManually()
    {
        AbstractEntity armeeliste = (AbstractEntity) DBFactory.getInstance().read(ArmyListEntity.class, 3);

        if (armeeliste == null) {
            return;
        }

        armeeliste = new ArmyListEntity(AbstractEntity.EntityType.ARMYLIST, "Armeeliste");

        /* Entry */
        AbstractEntity hq = new ArmyListEntity(AbstractEntity.EntityType.CATEGORY, "HQ");
        armeeliste.addEntity(hq);
        hq.setAttribute(1, Condition.ConditionTypes.MIN_COUNT);
        hq.setAttribute(2, Condition.ConditionTypes.MAX_COUNT);

        AbstractEntity elite = new ArmyListEntity(AbstractEntity.EntityType.CATEGORY, "Elite");
        armeeliste.addEntity(elite);
        elite.setAttribute(3, Condition.ConditionTypes.MIN_COUNT);

        AbstractEntity standard = new ArmyListEntity(AbstractEntity.EntityType.CATEGORY, "Standard");
        armeeliste.addEntity(standard);
        standard.setAttribute(2, Condition.ConditionTypes.MIN_COUNT);
        standard.setAttribute(6, Condition.ConditionTypes.MAX_COUNT);

        AbstractEntity sturm = new ArmyListEntity(AbstractEntity.EntityType.CATEGORY, "Sturm");
        armeeliste.addEntity(sturm);
        sturm.setAttribute(3, Condition.ConditionTypes.MAX_COUNT);

        AbstractEntity unterstuetzung = new ArmyListEntity(AbstractEntity.EntityType.CATEGORY, "Unterstützung");
        armeeliste.addEntity(unterstuetzung);
        unterstuetzung.setAttribute(3, Condition.ConditionTypes.MAX_COUNT);

        /* Unit */
        AbstractEntity wb = new ArmyListEntity(AbstractEntity.EntityType.NODE, "Warboss");
        wb.setAttribute(60, Condition.ConditionTypes.PRICE);
        hq.addEntity(wb);

        AbstractEntity fetterSpalta_wb = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Fetter Spalta");
        fetterSpalta_wb.setAttribute(1, Condition.ConditionTypes.MAX_COUNT);
        fetterSpalta_wb.setAttribute(5, Condition.ConditionTypes.PRICE);
        AbstractEntity energieKlaue_wb = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Energiekrallä");
        energieKlaue_wb.setAttribute(1, Condition.ConditionTypes.MAX_COUNT);
        energieKlaue_wb.setAttribute(25, Condition.ConditionTypes.PRICE);
        AbstractEntity orUpgrade2_wb = oneOfUpgrade();
        orUpgrade2_wb.addEntity(fetterSpalta_wb);
        orUpgrade2_wb.addEntity(energieKlaue_wb);
        AbstractEntity Bazz_Kombi_wb = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Bazzuka-Kombi");
        Bazz_Kombi_wb.setAttribute(1, Condition.ConditionTypes.MAX_COUNT);
        Bazz_Kombi_wb.setAttribute(5, Condition.ConditionTypes.PRICE);
        AbstractEntity Gitbrenna_Kombi_wb = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Gitbrenna-Kombi");
        Gitbrenna_Kombi_wb.setAttribute(1, Condition.ConditionTypes.MAX_COUNT);
        Gitbrenna_Kombi_wb.setAttribute(5, Condition.ConditionTypes.PRICE);
        AbstractEntity SyncWumme_wb = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Sync. Wumme");
        SyncWumme_wb.setAttribute(1, Condition.ConditionTypes.MAX_COUNT);
        SyncWumme_wb.setAttribute(5, Condition.ConditionTypes.PRICE);
        AbstractEntity orUpgrade3_wb = oneOfUpgrade();
        orUpgrade3_wb.addEntity(Bazz_Kombi_wb);
        orUpgrade3_wb.addEntity(Gitbrenna_Kombi_wb);
        orUpgrade3_wb.addEntity(SyncWumme_wb);
        AbstractEntity waaaghbike = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Waaaghbike");
        waaaghbike.setAttribute(1, Condition.ConditionTypes.MAX_COUNT);
        waaaghbike.setAttribute(40, Condition.ConditionTypes.PRICE);
        AbstractEntity megaruestung = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Megarüstung");
        megaruestung.setAttribute(1, Condition.ConditionTypes.MAX_COUNT);
        megaruestung.setAttribute(40, Condition.ConditionTypes.PRICE);

        AbstractEntity anyOfUpgrade_wb = anyOfUpgrade();
        anyOfUpgrade_wb.addEntity(orUpgrade2_wb);
        anyOfUpgrade_wb.addEntity(orUpgrade3_wb);
        anyOfUpgrade_wb.addEntity(waaaghbike);

        AbstractEntity orUpgrade1_wb = oneOfUpgrade();
        orUpgrade1_wb.addEntity(megaruestung);
        orUpgrade1_wb.addEntity(anyOfUpgrade_wb);

        AbstractEntity Munigrot_wb = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Munigrot");
        Munigrot_wb.setAttribute(1, Condition.ConditionTypes.MAX_COUNT);
        Munigrot_wb.setAttribute(3, Condition.ConditionTypes.PRICE);
        AbstractEntity Squik_wb = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Schnappasquik");
        Squik_wb.setAttribute(1, Condition.ConditionTypes.MAX_COUNT);
        Squik_wb.setAttribute(15, Condition.ConditionTypes.PRICE);
        AbstractEntity CyborgKoerpa_wb = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Cyborg Körpa");
        CyborgKoerpa_wb.setAttribute(1, Condition.ConditionTypes.MAX_COUNT);
        CyborgKoerpa_wb.setAttribute(10, Condition.ConditionTypes.PRICE);
        AbstractEntity stange_wb = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Trophäenstangä");
        stange_wb.setAttribute(1, Condition.ConditionTypes.MAX_COUNT);
        stange_wb.setAttribute(5, Condition.ConditionTypes.PRICE);
        AbstractEntity Panzaruestung_wb = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Panzarüstung");
        Panzaruestung_wb.setAttribute(1, Condition.ConditionTypes.MAX_COUNT);
        Panzaruestung_wb.setAttribute(5, Condition.ConditionTypes.PRICE);

        wb.addEntity(orUpgrade1_wb);
        wb.addEntity(Munigrot_wb);
        wb.addEntity(Squik_wb);
        wb.addEntity(CyborgKoerpa_wb);
        wb.addEntity(stange_wb);
        wb.addEntity(Panzaruestung_wb);

        AbstractEntity mek = new ArmyListEntity(AbstractEntity.EntityType.NODE, "Bigmek");
        mek.setAttribute(110, Condition.ConditionTypes.PRICE);
        hq.addEntity(mek);

        AbstractEntity fetterSpalta = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Fetter Spalta");
        fetterSpalta.setAttribute(1, Condition.ConditionTypes.MAX_COUNT);
        fetterSpalta.setAttribute(5, Condition.ConditionTypes.PRICE);
        AbstractEntity energieKlaue = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Energiekrallä");
        energieKlaue.setAttribute(1, Condition.ConditionTypes.MAX_COUNT);
        energieKlaue.setAttribute(25, Condition.ConditionTypes.PRICE);
        AbstractEntity stange = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Trophänstangä");
        stange.setAttribute(1, Condition.ConditionTypes.MAX_COUNT);
        stange.setAttribute(5, Condition.ConditionTypes.PRICE);
        AbstractEntity panza = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Panzarüstung");
        panza.setAttribute(1, Condition.ConditionTypes.MAX_COUNT);
        panza.setAttribute(5, Condition.ConditionTypes.PRICE);
        AbstractEntity orUpgrade2 = oneOfUpgrade();
        orUpgrade2.addEntity(fetterSpalta);
        orUpgrade2.addEntity(energieKlaue);

        AbstractEntity boyzBoss = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Boss");
        boyzBoss.addEntity(orUpgrade2);
        boyzBoss.addEntity(stange);
        boyzBoss.addEntity(panza);
        boyzBoss.setAttribute(1, Condition.ConditionTypes.MAX_COUNT);
        boyzBoss.setAttribute(10, Condition.ConditionTypes.PRICE);
        AbstractEntity boy = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Boy");
        boy.setAttribute(10, Condition.ConditionTypes.MIN_COUNT);
        boy.setAttribute(30, Condition.ConditionTypes.MAX_COUNT);
        boy.setAttribute(10, Condition.ConditionTypes.COUNT);
        boy.setAttribute(6, Condition.ConditionTypes.PRICE);
        boy.addEntity(boyzBoss);
        AbstractEntity stikkbombz = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Stikkbombz");

        @SuppressWarnings("unchecked")
        CalculatedNumber stikkbombzPrice = new CalculatedNumber((NumberCondition<Integer>) boy.getAttribute(Condition.ConditionTypes.COUNT), 1, CalculatedNumber.Arithmetic.MULTIPLY);
        stikkbombz.setAttribute(1, Condition.ConditionTypes.MAX_COUNT);
        stikkbombz.setAttribute(stikkbombzPrice, Condition.ConditionTypes.PRICE);

        // stikkbombzPrice.addChangeListener( stikkbombz );

        AbstractEntity wummen = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Wummen");
        wummen.setAttribute(1, Condition.ConditionTypes.MAX_COUNT);
        AbstractEntity fetteWumme = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Fette Wumme");
        AbstractEntity bazzukka = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Bazzukka");
        @SuppressWarnings({"unchecked", "rawtypes"})
        CalculatedNumber fwMax = new CalculatedNumber(new CalculatedNumber((NumberCondition) boy.getAttribute(Condition.ConditionTypes.COUNT), 10, CalculatedNumber.Arithmetic.DIVIDE), (NumberCondition) bazzukka.getAttribute(Condition.ConditionTypes.COUNT), CalculatedNumber.Arithmetic.SUBTRACT);
        // fwMax.addChangeListener( fetteWumme );
        fetteWumme.setAttribute(fwMax, Condition.ConditionTypes.MAX_COUNT);
        fetteWumme.setAttribute(5, Condition.ConditionTypes.PRICE);

        @SuppressWarnings({"unchecked", "rawtypes"})
        CalculatedNumber bazMax = new CalculatedNumber(new CalculatedNumber((NumberCondition) boy.getAttribute(Condition.ConditionTypes.COUNT), 10, CalculatedNumber.Arithmetic.DIVIDE), (NumberCondition) fetteWumme.getAttribute(Condition.ConditionTypes.COUNT), CalculatedNumber.Arithmetic.SUBTRACT);
        // bazMax.addChangeListener( bazzukka );
        bazzukka.setAttribute(bazMax, Condition.ConditionTypes.MAX_COUNT);
        bazzukka.setAttribute(10, Condition.ConditionTypes.PRICE);

        AbstractEntity boyz = new ArmyListEntity(AbstractEntity.EntityType.NODE, "Orkboyz");
        boyz.addEntity(boy);
        boyz.addEntity(stikkbombz);
        boyz.addEntity(wummen);
        boyz.addEntity(fetteWumme);
        boyz.addEntity(bazzukka);
        standard.addEntity(boyz);

        AbstractEntity bosse = new ArmyListEntity(AbstractEntity.EntityType.NODE, "Bosse");
        elite.addEntity(bosse);
        AbstractEntity boss = new ArmyListEntity(AbstractEntity.EntityType.UPGRADE, "Boss");
        boss.setAttribute(20, Condition.ConditionTypes.PRICE);
        bosse.addEntity(boss);

        DBFactory.getInstance().write(armeeliste);
    }

    private static AbstractEntity anyOfUpgrade()
    {
        return new ArmyListEntity(AbstractEntity.EntityType.ANYOFUPGRADE, "beliebiges aus");
    }

    private static AbstractEntity oneOfUpgrade()
    {
        return new ArmyListEntity(AbstractEntity.EntityType.ONEOFUPGRADE, "eines aus");
    }
}
