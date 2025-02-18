package software.xdev.vaadin.maps.leaflet.flow.demo;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import software.xdev.vaadin.maps.leaflet.flow.LMap;
import software.xdev.vaadin.maps.leaflet.flow.data.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Route("")
public class LeafletView extends VerticalLayout
{
	private boolean viewLunch = false;

	/**
	 * UI-Components
	 */
	private final Button btnLunch = new Button("Where do XDEV employees go for lunch?");
	private final Button btnZoomToExtent = new Button("Zoom to extent (fixed values)");
	private final Button btnZoomToExtent1 = new Button("Zoom to extent (1 value)");
	private final Button btnAddToZoomView = new Button("Add point");
	private final Button btnAddToZoomNullView = new Button("Clear points");
	private final Button btnOpenDialog = new Button("Open dialog over map", ev ->
	{
		final Icon icoClose = VaadinIcon.CLOSE.create();

		final Dialog dialog = new Dialog(icoClose);
		dialog.setWidth("50vw");
		dialog.setHeight("50vh");
		dialog.open();

		icoClose.addClickListener(iev -> dialog.close());
	});
	private final Button btnOpenZobPopup = new Button("Open ZoB popup");
	private final Button btnAddRemove = new Button("Add/remove marker");

	private LMap map;

	private LMarker markerZob;
	private LMarker markerRathaus;
	private LMarker markerNoTag;

	private LCircle circleRange;
	private LMarker markerPizza;
	private LMarker markerKebab;
	private LMarker markerAsia;
	private LMarker markerGreek;
	private LMarker markerBakery;
	private LMarker markerLeberkaese;
	private LBounds lbounds;

	public LeafletView()
	{
		this.btnLunch.addClickListener(this::btnLunchClick);
		this.btnZoomToExtent.addClickListener(this::btnZoomToExtentClick);
		this.btnZoomToExtent1.addClickListener(this::btnZoomToExtent1Click);
		this.btnAddToZoomView.addClickListener(this::btnAddToZoomViewClick);
		this.btnAddToZoomNullView.addClickListener(this::btnAddToZoomViewNullClick);
		this.btnOpenZobPopup.addClickListener(event -> map.openPopup(markerZob));
		this.btnAddRemove.addClickListener(this::btnAddRemoveClick);

		this.add(new HorizontalLayout(
				this.btnLunch,
				this.btnOpenDialog,
				this.btnOpenZobPopup,
				this.btnAddRemove,
				this.btnZoomToExtent,
				this.btnZoomToExtent1,
				this.btnAddToZoomView,
				this.btnAddToZoomNullView
		));

		this.initMapComponents();
	}

	private void btnLunchClick(final ClickEvent<Button> event)
	{
		this.viewLunch = !this.viewLunch;

		final List<LComponent> normalComponents = Arrays.asList(this.markerRathaus, this.markerZob);
		final List<LComponent> lunchComponents = Arrays.asList(
				this.circleRange,
				this.markerPizza,
				this.markerKebab,
				this.markerAsia,
				this.markerGreek,
				this.markerBakery,
				this.markerLeberkaese);

		this.map.setViewPoint(new LCenter(49.675126, 12.160733, this.viewLunch ? 16 : 17));
		this.map.removeLComponents(this.viewLunch ? normalComponents : lunchComponents);
		this.map.addLComponents(this.viewLunch ? lunchComponents : normalComponents);

		this.btnLunch.setText(this.viewLunch ? "Go back to the normal view" : "Where do XDEV employees go for lunch?");
	}

	private void btnZoomToExtentClick(final ClickEvent<Button> event)
	{
//		final LBounds bounds = new LBounds(
//			new LPoint(44.7099952002323, -0.5825165376485949),	// North
//			new LPoint(44.70578333435885, -0.5855568214725881)		// South
//			);
		lbounds = new LBounds(
			new LPoint(44.83422173810018,-0.570227273269922),
			new LPoint(44.83023173810018,-0.577257273169922)
//			new LPoint(44.718751793498356, -0.5944254275727441),	// North
//			new LPoint(44.71652566513751, -0.5853273748498107)		// South
			);
		this.map.zoomToExtent(lbounds);
	}
	private void btnZoomToExtent1Click(final ClickEvent<Button> event)
	{
		lbounds = new LBounds(
			new LPoint(44.7199952002323, -0.5925165376485949)
			);
		this.map.zoomToExtent(lbounds);
	}
	private void btnAddToZoomViewClick(final ClickEvent<Button> event)
	{
		if (lbounds == null)
			btnZoomToExtentClick(event);
		lbounds.addPoints(
				new LPoint(44.714464310360036, -0.5966272734835651),
				new LPoint(44.72142885939203, -0.579239682291169));
		this.map.zoomToExtent(lbounds);
	}
	private void btnAddToZoomViewNullClick(final ClickEvent<Button> event)
	{
		this.map.zoomToExtent(null);
	}


	private void btnAddRemoveClick(ClickEvent<Button> event)
	{
		if (map.containsLComponent(markerNoTag))
		{
			map.removeLComponents(markerNoTag);
		}
		else
		{
			map.addLComponents(markerNoTag);
		}
	}

	private void initMapComponents()
	{
		this.markerZob = new LMarker(49.673470, 12.160108, "ZoB");
		this.markerZob.setPopup("Central bus station");

		final LMarker markerXDev = new LMarker(49.675806677512824, 12.160990185846394);
		final LIcon xDevLogo = new LIcon("https://www.xing.com/img/custom/communities/communities_files/f/f/6/32758/large/XDEV_600x600_red.png?1438789458");

		markerNoTag = new LMarker(49.675328, 12.162112);

		// other option:
		// xDevLogo.setIconUrl(
		// "https://www.xing.com/img/custom/communities/communities_files/f/f/6/32758/large/XDEV_600x600_red.png?1438789458");
		xDevLogo.setIconSize(70, 70);
		xDevLogo.setIconAnchor(33, 55);
		markerXDev.setPopup("<a href='https://www.xdev-software.de/'>Xdev-Software GmbH</a>");
		markerXDev.setIcon(xDevLogo);

		final LMarker markerInfo = new LMarker(49.674095, 12.162257);
		final LDivIcon div = new LDivIcon(
				"<p><center><b>Welcome to Weiden in der Oberpfalz!</b></center></p><p>This Demo shows you different Markers,<br> Popups, Polygon and other Stuff</p>");

		// other options:
		// div.setIconSize(265, 90);
		// div.setHtml(
		// "<p><center><b>Welcome to Weiden in der Oberpfalz!</b></center></p><p>This Demo shows you different Markers,
		// Popups, Polygon and other Stuff</p>");
		markerInfo.setDivIcon(div);

		final LPolygon polygonNoc = new LPolygon(
				Arrays.asList(
						new LPoint(49.674910, 12.159202),
						new LPoint(49.675719, 12.160248),
						new LPoint(49.675962, 12.160033),
						new LPoint(49.675691, 12.158011),
						new LPoint(49.675306, 12.158499)));
		polygonNoc.setFill(true);
		polygonNoc.setFillColor("#3366ff");
		polygonNoc.setFillOpacity(0.8);
		polygonNoc.setStroke(false);
		polygonNoc.setPopup("NOC-Nordoberpfalz Center");

		final LPolyline polyline = new LPolyline(List.of(
				new LPoint(49.675786, 12.157965),
				new LPoint(49.676105, 12.159891),
				new LPoint(49.676827, 12.159188),
				new LPoint(49.676452, 12.157740)
		));
		polyline.setPopup("polyline test");
		polyline.setFill(true);
		polyline.setFillColor("#00ffff");

		final LPolyline polyline2 = new LPolyline(List.of(
				new LPoint(49.675995, 12.160095),
				new LPoint(49.676106, 12.160336),
				new LPoint(49.676484, 12.160648),
				new LPoint(49.677553, 12.165003)
		));
		polyline2.setPopup("line 2");
		polyline2.setStrokeColor("#ffff00");

		this.markerRathaus = new LMarker(49.675519, 12.163868, "L-22556");
		this.markerRathaus.setPopup("Old Town Hall");

		this.circleRange = new LCircle(49.675126, 12.160733, 450);

		this.markerPizza = new LMarker(49.674413, 12.160925);
		this.markerPizza.setPopup("Pizza!");
		this.markerPizza.setTooltip("Pizza?");

		this.markerKebab = new LMarker(49.673026, 12.156278);
		this.markerKebab.setPopup("Kebab!");

		this.markerAsia = new LMarker(49.675039, 12.162127);
		this.markerAsia.setPopup("Asian Food");

		this.markerGreek = new LMarker(49.675126, 12.161899);
		this.markerGreek.setPopup("Greek Food");

		this.markerBakery = new LMarker(49.674806, 12.160249);
		this.markerBakery.setPopup("Fresh baked stuff");

		this.markerLeberkaese = new LMarker(49.673800, 12.160113);
		this.markerLeberkaese.setPopup("Fast food like Leberkäsesemmeln");

		this.map = new LMap(49.675126, 12.160733, 17);
		this.map.setTileLayer(new LTileLayer(
				"https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
				"© <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a>",
				18,
				"base"
		));

		this.map.setSizeFull();

		this.markerZob.addClickListener(event -> System.out.println(this.markerZob.getTag()));
		polygonNoc.addClickListener(event -> System.out.println("Polygone clicked"));
		polyline.addClickListener(event -> System.out.println("Polyline clicked"));
		polyline2.addClickListener(event -> System.out.println("Polyline 2 clicked"));
		this.markerRathaus.addClickListener(event -> System.out.println(this.markerRathaus.getTag()));
		markerNoTag.addClickListener(event -> System.out.println("No tag clicked"));

		this.map.addMapClickListener(event ->
		{
			final Notification notification = new Notification("Map clicked here: " + event.getLat() + "°N, " + event.getLng() + "°E");
			notification.setDuration(5000);
			notification.open();
		});

		this.map.addLComponents(
				markerXDev,
				markerInfo,
				this.markerZob,
				polygonNoc,
				polyline,
				polyline2,
				this.markerRathaus,
				markerNoTag
		);

		this.add(this.map);
		this.setSizeFull();
	}
}
