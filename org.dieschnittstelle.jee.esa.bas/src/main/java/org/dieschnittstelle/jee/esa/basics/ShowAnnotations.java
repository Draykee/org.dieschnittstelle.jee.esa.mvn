package org.dieschnittstelle.jee.esa.basics;


import org.dieschnittstelle.jee.esa.basics.annotations.AnnotatedStockItemBuilder;
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
    private static void showAttributes( Object consumable )
    {

        show( "class is: " + consumable.getClass() );

        StringBuilder fieldStr = new StringBuilder();
        //Add classname
        fieldStr.append( consumable.getClass().getSimpleName() );

        //Add field names
        Arrays.asList( consumable.getClass().getDeclaredFields() ).forEach( field -> {
            try
            {
                //Override accessibility of the field to get its value
                field.setAccessible( true );
                fieldStr.append( String.format( " ,%s:%s", field.getName(), field.get( consumable ) ) );
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
