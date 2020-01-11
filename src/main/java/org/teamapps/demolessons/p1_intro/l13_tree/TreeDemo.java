package org.teamapps.demolessons.p1_intro.l13_tree;

import com.google.common.io.Files;
import org.teamapps.demolessons.DemoLesson;
import org.teamapps.icon.material.MaterialIcon;
import org.teamapps.server.jetty.embedded.TeamAppsJettyEmbeddedServer;
import org.teamapps.ux.component.Component;
import org.teamapps.ux.component.absolutelayout.Length;
import org.teamapps.ux.component.dummy.DummyComponent;
import org.teamapps.ux.component.flexcontainer.VerticalLayout;
import org.teamapps.ux.component.panel.Panel;
import org.teamapps.ux.component.template.BaseTemplate;
import org.teamapps.ux.component.template.BaseTemplateTreeNode;
import org.teamapps.ux.component.tree.SimpleTree;
import org.teamapps.ux.component.tree.SimpleTreeModel;
import org.teamapps.ux.component.tree.Tree;
import org.teamapps.ux.session.SessionContext;
import org.teamapps.webcontroller.SimpleWebController;

import java.util.Arrays;
import java.util.List;

public class TreeDemo implements DemoLesson {

    private Component rootComponent = new DummyComponent();
    private SessionContext context;

    public TreeDemo(SessionContext context) {
        this.context = context;


        /* SimpleTree */
        SimpleTree<Void> simpleTree = new SimpleTree<>();

        BaseTemplateTreeNode<Void> locations = new BaseTemplateTreeNode<Void>(MaterialIcon.MAP, "Locations");
        simpleTree.addNode(locations);
        simpleTree.addNode(new BaseTemplateTreeNode<Void>(MaterialIcon.LOCAL_AIRPORT, "Airport").setParent(locations));
        simpleTree.addNode(new BaseTemplateTreeNode<Void>(MaterialIcon.LOCAL_FLORIST, "Florist").setParent(locations));
        simpleTree.addNode(new BaseTemplateTreeNode<Void>(MaterialIcon.LOCAL_GROCERY_STORE, "Store").setParent(locations));
        simpleTree.setTemplatesByDepth(BaseTemplate.LIST_ITEM_MEDIUM_ICON_SINGLE_LINE, BaseTemplate.LIST_ITEM_SMALL_ICON_SINGLE_LINE);

        simpleTree.onNodeSelected.addListener(node -> context.showNotification(MaterialIcon.MAP, node.getCaption()));

        /* Tree with a Model */
        SimpleTreeModel<Void> simpleTreeModel = new SimpleTreeModel<>();
        simpleTreeModel.addNodes(createNodes());
        Tree<BaseTemplateTreeNode<Void>> tree = new Tree<>(simpleTreeModel);
        tree.setEntryTemplate(BaseTemplate.LIST_ITEM_MEDIUM_ICON_SINGLE_LINE);


        /* Add both Trees in a panel to a VerticalLayout */
        Panel panel1 = new Panel(MaterialIcon.LIGHTBULB_OUTLINE, "SimpleTree Demo");
        panel1.setContent(simpleTree);
        Panel panel2 = new Panel(MaterialIcon.DIRECTIONS_TRANSIT, "Transportation");
        panel2.setContent(tree);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(panel1);
        panel1.setMinHeight(new Length(200));

        verticalLayout.addComponent(panel2);
        panel2.setMinHeight(new Length(400));
        rootComponent = verticalLayout;

    }

    private static List<BaseTemplateTreeNode<Void>> createNodes() {
        BaseTemplateTreeNode<Void> meansOfTransport = new BaseTemplateTreeNode<Void>(MaterialIcon.DIRECTIONS, "Travel by Manpower");
        BaseTemplateTreeNode<Void> walking = new BaseTemplateTreeNode<Void>(MaterialIcon.DIRECTIONS_WALK, "Walking")
                .setParent(meansOfTransport);
        BaseTemplateTreeNode<Void> running = new BaseTemplateTreeNode<Void>(MaterialIcon.DIRECTIONS_RUN, "Running").setParent(meansOfTransport);
        BaseTemplateTreeNode<Void> bike = new BaseTemplateTreeNode<Void>(MaterialIcon.DIRECTIONS_BIKE, "Bike").setParent(meansOfTransport);
        BaseTemplateTreeNode<Void> publicTransportation = new BaseTemplateTreeNode<Void>(MaterialIcon.DIRECTIONS_TRANSIT, "Public Transportation");
        BaseTemplateTreeNode<Void> bus = new BaseTemplateTreeNode<Void>(MaterialIcon.DIRECTIONS_BUS, "BUS").setParent(publicTransportation);
        BaseTemplateTreeNode<Void> taxi = new BaseTemplateTreeNode<Void>(MaterialIcon.DIRECTIONS_CAR, "Taxi").setParent(publicTransportation);
        BaseTemplateTreeNode<Void> ship = new BaseTemplateTreeNode<Void>(MaterialIcon.DIRECTIONS_BOAT, "Ship").setParent(publicTransportation);
        BaseTemplateTreeNode<Void> railway = new BaseTemplateTreeNode<Void>(MaterialIcon.DIRECTIONS_RAILWAY, "Railway").setParent(publicTransportation);

        return Arrays.asList(
                meansOfTransport,
                walking,
                running,
                bike,
                publicTransportation,
                bus,
                taxi,
                ship,
                railway
        );
    }

    public Component getRootComponent(){
        return rootComponent;
    }

    // This method is called every time the Demo is selected in the DemoLessonApp
    public void handleDemoSelected() { }


    public static void main(String[] args) throws Exception {

        SimpleWebController controller = new SimpleWebController(context -> {

            TreeDemo textFieldDemo = new TreeDemo(context);
            textFieldDemo.handleDemoSelected();
            return textFieldDemo.getRootComponent();
        });
        new TeamAppsJettyEmbeddedServer(controller, Files.createTempDir()).start();
    }
}