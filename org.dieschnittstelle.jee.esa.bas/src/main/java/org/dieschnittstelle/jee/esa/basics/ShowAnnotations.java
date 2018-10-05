package org.dieschnittstelle.jee.esa.basics;


import org.dieschnittstelle.jee.esa.basics.annotations.AnnotatedStockItemBuilder;
import org.dieschnittstelle.jee.esa.basics.annotations.DisplayAs;
import org.dieschnittstelle.jee.esa.basics.annotations.StockItemProxyImpl;

import java.util.Arrays;

import static org.dieschnittstelle.jee.esa.utils.Utils.show;

public class ShowAnnotations
{

    public static void main( String[] args )
    {
        // we initialise the collection
        StockItemCollection collection = new StockItemCollection(
                "stockitems_annotations.xml", new AnnotatedStockItemBuilder() );
        // we load the contents into the collection
        collection.load();

        for ( IStockItem consumable : collection.getStockItems() )
        {
            showAttributes( ( (StockItemProxyImpl) consumable ).getProxiedObject() );
        }

        // we initialise a consumer
        Consumer consumer = new Consumer();
        // ... and let them consume
        consumer.doShopping( collection.getStockItems() );
    }

    /*
     * UE BAS2
     */
    /*
    private static void showAttributes( Object consumable )
    {

        show( "class is: " + consumable.getClass() );

        StringBuilder fieldStr = new StringBuilder();
        //Add classname
        fieldStr.append( consumable.getClass().getSimpleName() );

        //Add field names
        Arrays.stream( consumable.getClass().getDeclaredFields() ).forEach( field -> {
            try
            {
                //Override accessibility of the field to get its value
                field.setAccessible( true );
                fieldStr.append( String.format( ", %s:%s", field.getName(), field.get( consumable ) ) );
                //Make it "private" again
                field.setAccessible( false );
            }
            catch ( IllegalAccessException e )
            {
                e.printStackTrace();
            }
        } );

        show( String.format( "{%s}", fieldStr.toString() ) );
    }
    */

    /*
     * UE BAS3
     * For tests:   Set DisplayAs Annotation at Schokolade.marke to "Typ"
     *              Set DisplayAs Annotation at Milch.menge to "Flaschen"
     */
    private static void showAttributes( Object consumable )
    {
        show( "class is: " + consumable.getClass() );

        StringBuilder fieldStr = new StringBuilder();
        //Add classname
        fieldStr.append( consumable.getClass().getSimpleName() );

        //Add field names
        Arrays.stream( consumable.getClass().getDeclaredFields() ).forEach( field -> {
            try
            {
                DisplayAs display = field.getAnnotation( DisplayAs.class );
                //Override accessibility of the field to get its value
                field.setAccessible( true );
                fieldStr.append( String.format( ", %s:%s", display != null ? display.value() : field.getName(), field.get( consumable ) ) );
                //Make it "private" again
                field.setAccessible( false );
            }
            catch ( IllegalAccessException e )
            {
                e.printStackTrace();
            }
        } );

        show( String.format( "{%s}", fieldStr.toString() ) );
    }

}
