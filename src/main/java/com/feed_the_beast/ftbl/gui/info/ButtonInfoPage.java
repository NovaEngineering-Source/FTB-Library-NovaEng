package com.feed_the_beast.ftbl.gui.info;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.client.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.api.info.impl.InfoPageHelper;
import com.feed_the_beast.ftbl.util.TextureCoords;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.AbstractMap;
import java.util.List;

/**
 * Created by LatvianModder on 04.03.2016.
 */
public class ButtonInfoPage extends ButtonLM
{
    public final GuiInfo guiInfo;
    public final String pageID;
    public final IGuiInfoPage page;
    public String hover;
    public TextureCoords icon;
    public boolean iconBlur = false;
    private boolean prevMouseOver = false;

    public ButtonInfoPage(GuiInfo g, String id, IGuiInfoPage p, TextureCoords t)
    {
        super(0, g.panelPages.height, g.panelWidth - 36, t == null ? 13 : 18);
        guiInfo = g;
        pageID = id;
        page = p;
        icon = t;
        updateTitle(g);
    }

    public ButtonInfoPage setIconBlur()
    {
        iconBlur = true;
        return this;
    }

    @Override
    public void onClicked(@Nonnull GuiLM gui, @Nonnull MouseButton button)
    {
        GuiLM.playClickSound();

        page.refreshGui(guiInfo);

        if(page.getPages().isEmpty())
        {
            guiInfo.selectedPage = new AbstractMap.SimpleEntry<>(pageID, page);
            guiInfo.sliderText.value = 0F;
            guiInfo.panelText.posY = 10;
            guiInfo.panelText.refreshWidgets();
        }
        else
        {
            new GuiInfo(guiInfo, new AbstractMap.SimpleEntry<>(pageID, page)).openGui();
        }
    }

    public void updateTitle(@Nonnull GuiLM gui)
    {
        ITextComponent titleC = InfoPageHelper.getTitleComponent(page, pageID);
        if(guiInfo.selectedPage == page)
        {
            titleC.getStyle().setBold(true);
        }

        if(gui.isMouseOver(this))
        {
            titleC.getStyle().setUnderlined(true);
        }

        title = titleC.getFormattedText();
        hover = null;

        if(guiInfo.font.getStringWidth(title) > width)
        {
            hover = InfoPageHelper.getTitleComponent(page, pageID).getFormattedText();
        }
    }

    @Override
    public void addMouseOverText(@Nonnull GuiLM gui, @Nonnull List<String> l)
    {
        if(hover != null)
        {
            l.add(hover);
        }
    }

    @Override
    public boolean shouldRender(@Nonnull GuiLM gui)
    {
        return parentPanel.isInside(this);
    }

    @Override
    public void renderWidget(@Nonnull GuiLM gui)
    {
        boolean mouseOver = gui.isMouseOver(this);

        if(prevMouseOver != mouseOver)
        {
            updateTitle(gui);
            prevMouseOver = mouseOver;
        }

        double ay = getAY();
        double ax = getAX();

        if(icon != null)
        {
            GlStateManager.color(1F, 1F, 1F, 1F);
            FTBLibClient.setTexture(icon.texture);

            if(iconBlur)
            {
                GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            }

            GuiLM.render(icon, ax + 1, ay + 1, 16, 16);

            if(iconBlur)
            {
                GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            }

            guiInfo.font.drawString(title, (int) ax + 19, (int) ay + 6, guiInfo.colorText);
        }
        else
        {
            GlStateManager.color(1F, 1F, 1F, 1F);
            guiInfo.font.drawString(title, (int) ax + 1, (int) ay + 1, guiInfo.colorText);
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
    }
}