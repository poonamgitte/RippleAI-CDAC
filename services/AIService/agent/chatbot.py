from typing import Dict

from langchain_groq import ChatGroq
from util.tools import tools
from util.state import PostState
from langchain_core.messages import HumanMessage, ToolMessage
import logging
import time
import os

from langchain_google_genai import ChatGoogleGenerativeAI


google_api_key = os.getenv("GOOGLE_API_KEY")
groq_api_key = os.getenv("GROQ_API_KEY")

# ------------ Logging ------------
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

llm_google = ChatGoogleGenerativeAI(
    model="gemini-2.0-flash",
    api_key=google_api_key,
    temperature=0.7,
)


llm_groq = ChatGroq(
    model="openai/gpt-oss-120b",  
    api_key=groq_api_key,
    temperature=0.7,
)


llm_with_tools = llm_groq.bind_tools(tools=tools)


def chatbot(state: PostState) -> Dict:
    logger.info("Chatbot node running...")
    try:
        clean_messages = []
        for msg in state["messages"]:
            # Convert tool outputs into readable human messages for Gemini
            if isinstance(msg, ToolMessage):
                clean_messages.append(
                    HumanMessage(content=f"Tool '{msg.name}' returned: {msg.content}")
                )
            else:
                clean_messages.append(msg)

        response = llm_with_tools.invoke(clean_messages)
        return {"messages": state["messages"] + [response]}

    except Exception as e:
        logger.error(f"Error in chatbot: {e}")
        return {"messages": state["messages"] + [HumanMessage(content=str(e))]}

